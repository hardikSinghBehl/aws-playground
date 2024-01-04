package com.behl.cipherinator.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.any;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import com.behl.cipherinator.dto.UserCreationRequestDto;
import com.behl.cipherinator.dto.UserLoginRequestDto;
import com.behl.cipherinator.entity.User;
import com.behl.cipherinator.exception.InvalidLoginCredentialException;
import com.behl.cipherinator.exception.UserNameAlreadyExistsException;
import com.behl.cipherinator.repository.UserRepository;

import net.bytebuddy.utility.RandomString;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@SpyBean
	private UserRepository userRepository;
	
	@SpyBean
	private EnvelopeEncryptionService envelopeEncryptionService;
	
	private static LocalStackContainer localStackContainer;

	static {
		localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:3"))
				.withCopyFileToContainer(MountableFile.forClasspathResource("init-kms.sh", 0744), "/etc/localstack/init/ready.d/init-kms.sh")
				.withCopyFileToContainer(MountableFile.forClasspathResource("init-dynamodb.sh", 0744), "/etc/localstack/init/ready.d/init-dynamodb.sh")
				.withServices(Service.KMS, Service.DYNAMODB)
				.waitingFor(Wait.forLogMessage(".*Executed init-kms.sh.*", 1))
				.waitingFor(Wait.forLogMessage(".*Executed init-dynamodb.sh.*", 1));
		localStackContainer.start();
	}

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("com.behl.cipherinator.aws.access-key", localStackContainer::getAccessKey);
		registry.add("com.behl.cipherinator.aws.secret-access-key", localStackContainer::getSecretKey);
		registry.add("com.behl.cipherinator.aws.region", localStackContainer::getRegion);
		registry.add("com.behl.cipherinator.aws.endpoint", localStackContainer::getEndpoint);
		
		// Key ID as configured in src/test/resources/init-kms.sh
		registry.add("com.behl.cipherinator.aws.kms.key-id", () -> "00000000-1111-2222-3333-000000000000");
	}
	
	@Test
	void shouldCreateUserRecordSuccessfully() {
		// prepare user creation request
		final var userName = RandomString.make();
		final var plainTextPassword = RandomString.make();
		final var userCreationRequest = new UserCreationRequestDto();
		userCreationRequest.setUserName(userName);
		userCreationRequest.setPassword(plainTextPassword);
		
		// call method under test
		userService.create(userCreationRequest);
		
		// verify interactions with spy beans
		verify(userRepository, times(1)).existsByUserName(userName);
		verify(envelopeEncryptionService, times(1)).encrypt(plainTextPassword);
		verify(userRepository, times(1)).save(Mockito.any(User.class));
		
		// assert record existence in datasource
		final Optional<User> retrievedUserRecord = userRepository.findByUserName(userName);
		assertThat(retrievedUserRecord).isPresent().get().satisfies(retrievedUser -> {
			assertThat(retrievedUser.getUserName()).isEqualTo(userName);
			assertThat(retrievedUser.getPassword()).isNotBlank().isNotEqualTo(plainTextPassword);
			assertThat(retrievedUser.getEncryptedDataKey()).isNotBlank();
		});
	}
	
	@Test
	void shouldNotCreateUserRecordForExistingUserName() {
		// prepare user creation request
		final var userName = RandomString.make();
		final var userCreationRequest = new UserCreationRequestDto();
		userCreationRequest.setUserName(userName);
		userCreationRequest.setPassword(RandomString.make());
		
		// create user record in datasource
		userService.create(userCreationRequest);

		// clear spy bean interactions before invoking method under test
		Mockito.clearInvocations(userRepository);
		
		// invoke method under test with same user creation request and assert exception
		final var exception = assertThrows(UserNameAlreadyExistsException.class, () -> userService.create(userCreationRequest));
		assertThat(exception.getReason()).isEqualTo("Account with given username already exists.");
		
		// verify interactions with spy beans
		verify(userRepository, times(1)).existsByUserName(userName);
		verify(userRepository, times(0)).save(Mockito.any(User.class));
	}
	
	@Test
	void shouldSucceedForValidLoginCredentials() {
		// prepare user creation request
		final var userName = RandomString.make();
		final var password = RandomString.make();
		final var userCreationRequest = new UserCreationRequestDto();
		userCreationRequest.setUserName(userName);
		userCreationRequest.setPassword(password);
		
		// create user record in datasource
		userService.create(userCreationRequest);
		
		// clear spy bean interactions before invoking method under test
		Mockito.clearInvocations(userRepository);
		Mockito.clearInvocations(envelopeEncryptionService);
		
		// prepare request with valid login credentials
		final var userLoginRequest = new UserLoginRequestDto();
		userLoginRequest.setUserName(userName);
		userLoginRequest.setPassword(password);
		
		// invoke method under test
		userService.login(userLoginRequest);
		
		// verify interactions with spy beans
		verify(userRepository, times(1)).findByUserName(userName);
		verify(envelopeEncryptionService, times(1)).decrypt(any(String.class), any(String.class));
	}
	
	@Test
	void shouldFailForInvalidLoginUserName() {
		// prepare login request with invalid username
		final var userName = RandomString.make();
		final var userLoginRequest = new UserLoginRequestDto();
		userLoginRequest.setUserName(userName);
		
		// invoke method under test and assert exception
		final var exception = assertThrows(InvalidLoginCredentialException.class, () -> userService.login(userLoginRequest));
		assertThat(exception.getReason()).isEqualTo("Invalid login credentials provided.");
		
		// verify interactions with spy beans
		verify(userRepository, times(1)).findByUserName(userName);
		verify(envelopeEncryptionService, times(0)).decrypt(any(String.class), any(String.class));
	}
	
	@Test
	void shouldFailForInvalidLoginPassword() {
		// prepare user creation request
		final var userName = RandomString.make();
		final var legitimatePassword = RandomString.make();
		final var userCreationRequest = new UserCreationRequestDto();
		userCreationRequest.setUserName(userName);
		userCreationRequest.setPassword(legitimatePassword);
		
		// create user record in datasource
		userService.create(userCreationRequest);

		// clear spy bean interactions before invoking method under test
		Mockito.clearInvocations(userRepository);
		Mockito.clearInvocations(envelopeEncryptionService);
		
		// prepare login request with valid username but invalid password
		final var incorrectPassword = RandomString.make();
		final var userLoginRequest = new UserLoginRequestDto();
		userLoginRequest.setUserName(userName);
		userLoginRequest.setPassword(incorrectPassword);
		assertThat(incorrectPassword).isNotEqualTo(legitimatePassword);
		
		// invoke method under test and assert exception
		final var exception = assertThrows(InvalidLoginCredentialException.class, () -> userService.login(userLoginRequest));
		assertThat(exception.getReason()).isEqualTo("Invalid login credentials provided.");
		
		// verify interactions with spy beans
		verify(userRepository, times(1)).findByUserName(userName);
		verify(envelopeEncryptionService, times(1)).decrypt(any(String.class), any(String.class));
	}

}
