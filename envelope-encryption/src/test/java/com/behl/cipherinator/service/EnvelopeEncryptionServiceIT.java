package com.behl.cipherinator.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

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

import com.behl.cipherinator.dto.EncryptionResultDto;

import net.bytebuddy.utility.RandomString;

@SpringBootTest
@ActiveProfiles("test")
class EnvelopeEncryptionServiceIT {
	
	@Autowired
	private EnvelopeEncryptionService envelopeEncryptionService;

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
	void shouldPerformEnvelopeEncryptionSuccessfully() {
		// generate random plain-text data
		final var data = RandomString.make();
		
		// encrypt plain-text data
		final var encryptionResult = envelopeEncryptionService.encrypt(data);
		
		// assert encrypted data and encrypted data key are Base64-encoded
		final var encryptedData = encryptionResult.getEncryptedData();
		final var encryptedDataKey = encryptionResult.getEncryptedDataKey();
		assertThat(encryptedData).isNotBlank().isBase64();
		assertThat(encryptedDataKey).isNotBlank().isBase64();
		
		// decrypt encrypted data back to plain-text
		final var decryptedData = envelopeEncryptionService.decrypt(encryptedData, encryptedDataKey);
		
		// assert decrypted data matches original generated plain-text data
		assertThat(decryptedData).isNotBlank().isEqualTo(data);
	}
	
	@Test
	void shouldGenerateUniqueEncryptionResultsForSamePlainTextData() {
		// generate random plain-text data
		final var data = RandomString.make();
		
		// encrypt plain-text data by calling method under test multiple times
		final var encryptionResults = new ArrayList<EncryptionResultDto>();
		for (int i = 0; i < 100; i++) {
			final var encryptionResult = envelopeEncryptionService.encrypt(data);
			encryptionResults.add(encryptionResult);
		}
		
		// assert uniqueness of encrypted data and encrypted data key
		assertThat(encryptionResults).map(EncryptionResultDto::getEncryptedData).doesNotHaveDuplicates();
		assertThat(encryptionResults).map(EncryptionResultDto::getEncryptedDataKey).doesNotHaveDuplicates();
	}

}