package com.behl.cipherinator.utility;

import static org.assertj.core.api.Assertions.assertThat;

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

import com.behl.cipherinator.annotation.Encryptable;
import com.behl.cipherinator.annotation.EncryptedDataKey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;

@SpringBootTest
@ActiveProfiles("test")
class FieldEncryptionManagerIT {
	
	@Autowired
	private FieldEncryptionManager fieldEncryptionManager;

	private static LocalStackContainer localStackContainer;

	static {
		localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:3"))
				.withCopyFileToContainer(MountableFile.forClasspathResource("init-kms.sh", 0744), "/etc/localstack/init/ready.d/init-kms.sh")
				.withServices(Service.KMS)
				.waitingFor(Wait.forLogMessage(".*Executed init-kms.sh.*", 1));
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
	void shouldEncryptAndDecryptAnnotatedFields() {
		// prepare object with plaintext values
		final var plaintextFirstName = RandomString.make();
		final var plaintextLastName = RandomString.make();
		final var person = new Person(plaintextFirstName, plaintextLastName);
		
		// encrypt the object by calling the method under test 
		fieldEncryptionManager.encryptFields(person);
		
		// assert the field values in the object are encrypted
		assertThat(person.getFirstName()).isNotBlank().isNotEqualTo(plaintextFirstName).isBase64();
		assertThat(person.getLastName()).isNotBlank().isNotEqualTo(plaintextLastName).isBase64();
		
		// decrypt the object by calling the method under test
		fieldEncryptionManager.decryptFields(person);
		
		// assert the field values in the object are decrypted back to their original value
		assertThat(person.getFirstName()).isNotBlank().isEqualTo(plaintextFirstName);
		assertThat(person.getLastName()).isNotBlank().isEqualTo(plaintextLastName);
	}
	
	@Getter
	@RequiredArgsConstructor
	class Person {
		
		@Encryptable
		private final String firstName;
		
		@Encryptable
		private final String lastName;
		
		@EncryptedDataKey
		private String dataKey;
				
	}
	
}
