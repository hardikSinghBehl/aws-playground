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

import com.behl.cipherinator.entity.MedicalRecord;

import net.bytebuddy.utility.RandomString;

@SpringBootTest
@ActiveProfiles("test")
class MedicalRecordRepositoryIT {

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

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
	void shouldReturnEmptyOptionalForNonExistingMedicalRecord() {
		// generate random medical record Id
		final var medicalRecordId = RandomString.make();

		// call method under test
		final Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(medicalRecordId);

		// assert response
		assertThat(medicalRecord).isEmpty();
	}

	@Test
	void shouldSaveMedicalRecordSuccessfully() {
		// create medical record
		final var medicalRecordId = RandomString.make();
		final var patientName = RandomString.make();
		final var medicalHistory = RandomString.make();
		final var encryptedDataKey = RandomString.make();
		final var medicalRecord = new MedicalRecord();
		medicalRecord.setId(medicalRecordId);
		medicalRecord.setPatientName(patientName);
		medicalRecord.setMedicalHistory(medicalHistory);
		medicalRecord.setEncryptedDataKey(encryptedDataKey);

		// call method under test
		medicalRecordRepository.save(medicalRecord);

		// assert record existence in datasource
		final Optional<MedicalRecord> retrievedMedicalRecord = medicalRecordRepository.findById(medicalRecordId);
		assertThat(retrievedMedicalRecord).isPresent().get().satisfies(record -> {
			assertThat(record.getId()).isEqualTo(medicalRecordId);
			assertThat(record.getPatientName()).isEqualTo(patientName);
			assertThat(record.getMedicalHistory()).isEqualTo(medicalHistory);
			assertThat(record.getEncryptedDataKey()).isEqualTo(encryptedDataKey);
		});
	}

}
