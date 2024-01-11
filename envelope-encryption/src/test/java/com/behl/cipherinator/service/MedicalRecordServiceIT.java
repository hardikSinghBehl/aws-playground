package com.behl.cipherinator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
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

import com.behl.cipherinator.dto.MedicalRecordCreationDto;
import com.behl.cipherinator.entity.MedicalRecord;
import com.behl.cipherinator.exception.InvalidMedicalRecordIdException;
import com.behl.cipherinator.repository.MedicalRecordRepository;
import com.behl.cipherinator.service.EnvelopeEncryptionService.Encryptor;
import com.behl.cipherinator.utility.FieldEncryptionManager;

import net.bytebuddy.utility.RandomString;

@SpringBootTest
@ActiveProfiles("test")
class MedicalRecordServiceIT {
	
	@Autowired
	private MedicalRecordService medicalRecordService;
	
	@SpyBean
	private FieldEncryptionManager fieldEncryptionManager;
	
	@SpyBean
	private MedicalRecordRepository medicalRecordRepository;
	
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
	void shouldCreateMedicalRecordSuccessfully() {
		// prepare medical record creation request
		final var patientName = RandomString.make();
		final var medicalHistory = RandomString.make();
		final var medicalCreationRequest = new MedicalRecordCreationDto();
		medicalCreationRequest.setPatientName(patientName);
		medicalCreationRequest.setMedicalHistory(medicalHistory);
		
		// call method under test
		final var medicalId = medicalRecordService.create(medicalCreationRequest);
		
		// verify interactions with spy beans
		verify(envelopeEncryptionService, times(1)).getEncryptor();
		verify(fieldEncryptionManager, times(1)).encryptFields(any(MedicalRecord.class), any(Encryptor.class));
		verify(medicalRecordRepository, times(1)).save(any(MedicalRecord.class));
		
		// assert record existence in datasource
		final var retrievedMedicalRecord = medicalRecordRepository.findById(medicalId);
		assertThat(retrievedMedicalRecord).isPresent().get().satisfies(retrievedRecord -> {
			assertThat(retrievedRecord.getId()).isNotBlank();
			assertThat(retrievedRecord.getEncryptedDataKey()).isNotBlank().isBase64();
			
			// assert fields annotated with @Encryptable are encrypted and not in their plaintext form
			assertThat(retrievedRecord.getPatientName()).isNotBlank().isNotEqualTo(patientName).isBase64();
			assertThat(retrievedRecord.getMedicalHistory()).isNotBlank().isNotEqualTo(medicalHistory).isBase64();
		});
	}
	
	@Test
	void shouldRetrieveMedicalRecordSavedInTheDatasource() {
		// save medical record in datasource
		final var patientName = RandomString.make();
		final var medicalHistory = RandomString.make();
		final var medicalCreationRequest = new MedicalRecordCreationDto();
		medicalCreationRequest.setPatientName(patientName);
		medicalCreationRequest.setMedicalHistory(medicalHistory);
		final var medicalId = medicalRecordService.create(medicalCreationRequest);
				
		// call method under test
		final var retievedMedicalRecord = medicalRecordService.retrieve(medicalId);
		
		// assert the retrieved medical record contains the original plaintext values
		assertThat(retievedMedicalRecord).isNotNull();
		assertThat(retievedMedicalRecord.getPatientName()).isNotBlank().isEqualTo(patientName);
		assertThat(retievedMedicalRecord.getMedicalHistory()).isNotBlank().isEqualTo(medicalHistory);
	}
	
	@Test
	void shouldThrowExceptionForInvalidMedicalRecordId() {
		// prepare invalid medical record id
		final var medicalRecordId = UUID.randomUUID().toString();
		
		// invoke method under test and assert exception
		final var exception = assertThrows(InvalidMedicalRecordIdException.class, () -> medicalRecordService.retrieve(medicalRecordId));
		assertThat(exception.getReason()).isEqualTo("Invalid medical record Id provided.");
		
		// verify interactions with spy beans
		verify(medicalRecordRepository, times(1)).findById(medicalRecordId);
	}

}
