package com.behl.repository;

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

import com.behl.entity.MedicalRecord;

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
		registry.add("com.behl.aws.access-key", localStackContainer::getAccessKey);
		registry.add("com.behl.aws.secret-access-key", localStackContainer::getSecretKey);
		registry.add("com.behl.aws.region", localStackContainer::getRegion);
		registry.add("com.behl.aws.endpoint", localStackContainer::getEndpoint);
	}
	
	@Test
	void shouldReturnEmptyOptionalForNonExistingMedicalRecord() {
	    // generate random medical record Id
	    var medicalRecordId = RandomString.make();

	    // call method under test
	    Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(medicalRecordId);

	    // assert response
	    assertThat(medicalRecord).isEmpty();
	}
	
	@Test
	void shouldSaveMedicalRecordSuccessfully() {
	    // create medical record
		var medicalRecord = new MedicalRecord();
	    var medicalRecordId = RandomString.make();
	    var patientName = RandomString.make();
	    var diagnosis = RandomString.make();
	    medicalRecord.setId(medicalRecordId);
	    medicalRecord.setPatientName(patientName);
	    medicalRecord.setDiagnosis(diagnosis);

	    // call method under test
	    medicalRecordRepository.save(medicalRecord);

	    // assert record existence in datasource
	    Optional<MedicalRecord> retrievedMedicalRecord = medicalRecordRepository.findById(medicalRecordId);
	    assertThat(retrievedMedicalRecord).isPresent().get().satisfies(record -> {
	        assertThat(record.getId()).isEqualTo(medicalRecordId);
	        assertThat(record.getPatientName()).isEqualTo(patientName);
	        assertThat(record.getDiagnosis()).isEqualTo(diagnosis);
	    });
	}

}