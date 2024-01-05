package com.behl.cipherinator.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import com.behl.cipherinator.entity.User;

import net.bytebuddy.utility.RandomString;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryIT {

	@Autowired
	private UserRepository userRepository;

	private static LocalStackContainer localStackContainer;

	static {
		localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:3"))
				.withCopyFileToContainer(MountableFile.forClasspathResource("init-dynamodb.sh", 0744), "/etc/localstack/init/ready.d/init-dynamodb.sh")
				.withServices(Service.DYNAMODB)
				.waitingFor(Wait.forLogMessage(".*Executed init-dynamodb.sh.*", 1));
		localStackContainer.start();
	}

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("com.behl.cipherinator.aws.access-key", localStackContainer::getAccessKey);
		registry.add("com.behl.cipherinator.aws.secret-access-key", localStackContainer::getSecretKey);
		registry.add("com.behl.cipherinator.aws.region", localStackContainer::getRegion);
		registry.add("com.behl.cipherinator.aws.endpoint", localStackContainer::getEndpoint);
	}

	@Test
	void shouldReturnEmptyOptionalForNonExistingUserName() {
		// generate random user-name
		final var userName = RandomString.make();

		// call method under test
		final Optional<User> user = userRepository.findByUserName(userName);

		// assert response
		assertThat(user).isEmpty();
	}

	@Test
	void shouldSaveUserSuccessfully() {
		// create user record
		final var userName = RandomString.make();
		final var password = RandomString.make();
		final var encryptedDataKey = RandomString.make();
		final var user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		user.setEncryptedDataKey(encryptedDataKey);

		// call method under test
		userRepository.save(user);

		// assert record existence in datasource
		final Optional<User> retrievedUserRecord = userRepository.findByUserName(userName);
		assertThat(retrievedUserRecord).isPresent().get().satisfies(retrievedUser -> {
			assertThat(retrievedUser.getUserName()).isEqualTo(userName);
			assertThat(retrievedUser.getPassword()).isEqualTo(password);
			assertThat(retrievedUser.getEncryptedDataKey()).isEqualTo(encryptedDataKey);
		});
	}

	@Test
	void shouldReturnTrueIfUserExistsByUserName() {
		// create user record in datasource
		final var userName = RandomString.make();
		final var user = new User();
		user.setUserName(userName);
		userRepository.save(user);

		// call method under test
		final var userNameExists = userRepository.existsByUserName(userName);

		// assert response
		assertThat(userNameExists).isTrue();
	}

	@Test
	void shouldReturnFalseIfUserRecordDoesNotExistByUserName() {
		// generate random user-name
		final var userName = RandomString.make();

		// call method under test
		final var userNameExists = userRepository.existsByUserName(userName);

		// assert response
		assertThat(userNameExists).isFalse();
	}

}
