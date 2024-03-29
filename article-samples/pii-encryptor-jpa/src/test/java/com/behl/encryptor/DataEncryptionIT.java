package com.behl.encryptor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

import com.behl.encryptor.annotation.Encryptable;
import com.behl.encryptor.annotation.EncryptedDataKey;
import com.behl.encryptor.helper.InitializeKMSKey;
import com.behl.encryptor.helper.InitializeMysqlContainer;
import com.behl.encryptor.listener.FieldEncryptionListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import junit.framework.AssertionFailedError;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.utility.RandomString;

@SpringBootTest
@InitializeKMSKey
@ActiveProfiles("test")
@InitializeMysqlContainer
class DataEncryptionIT {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Test
	void shoudEncryptPhiFieldsInDatabase() {
		var appointment = createAppointment();
		appointmentRepository.save(appointment);

		var sqlQuery = "SELECT diagnosis, treatment_plan, data_key FROM appointments WHERE id = ?";
		var query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter(1, appointment.getId());

		var result = (Object[]) query.getSingleResult();
		var fetchedDiagnosis = String.valueOf(result[0]);
		var fetchedTreatmentPlan = String.valueOf(result[1]);
		var fetchedDataKey = String.valueOf(result[2]);

		assertThat(fetchedDiagnosis).isNotBlank().isNotEqualTo(appointment.getDiagnosis());
		assertThat(fetchedTreatmentPlan).isNotBlank().isNotEqualTo(appointment.getTreatmentPlan());
		assertThat(fetchedDataKey).isNotBlank();
	}

	@Test
	void shouldPopulateEncryptedDataKey() {
		var appointment = createAppointment();

		assertThat(appointment.getDataKey()).isNull();

		var savedAppointment = appointmentRepository.save(appointment);

		assertThat(savedAppointment.getDataKey()).isNotBlank();
	}

	@Test
	void entityListenerShouldEncryptEncryptableFields() {
		var appointment = createAppointment();
		var savedAppointment = appointmentRepository.save(appointment);

		assertThat(savedAppointment.getDiagnosis()).isNotBlank().isNotEqualTo(appointment.getDiagnosis());
		assertThat(savedAppointment.getTreatmentPlan()).isNotBlank().isNotEqualTo(appointment.getTreatmentPlan());
		assertThat(savedAppointment.getDataKey()).isNotBlank();
	}

	@Test
	void shouldDecryptEncryptableFieldsJpaById() {
		var appointment = createAppointment();
		appointmentRepository.save(appointment);

		var retrievedAppointment = appointmentRepository.findById(appointment.getId()).orElseThrow(AssertionFailedError::new);

		assertThat(retrievedAppointment.getDiagnosis()).isNotBlank().isEqualTo(appointment.getDiagnosis());
		assertThat(retrievedAppointment.getTreatmentPlan()).isNotBlank().isEqualTo(appointment.getTreatmentPlan());
		assertThat(retrievedAppointment.getDataKey()).isNotBlank();
	}

	@Test
	void shouldDecryptEncryptableFieldsEntityManagerFind() {
		var appointment = createAppointment();
		appointmentRepository.save(appointment);

		var retrievedAppointment = entityManager.find(Appointment.class, appointment.getId());

		assertThat(retrievedAppointment.getDiagnosis()).isNotBlank().isEqualTo(appointment.getDiagnosis());
		assertThat(retrievedAppointment.getTreatmentPlan()).isNotBlank().isEqualTo(appointment.getTreatmentPlan());
		assertThat(retrievedAppointment.getDataKey()).isNotBlank();
	}

	@Test
	void shouldDecryptEncryptableFieldsEntityManagerNativeQuery() {
		var appointment = createAppointment();
		appointmentRepository.save(appointment);

		var sqlQuery = "SELECT * FROM appointments WHERE id = ?";
		var query = entityManager.createNativeQuery(sqlQuery, Appointment.class);
		query.setParameter(1, appointment.getId());
		var retrievedAppointment = (Appointment) query.getSingleResult();

		assertThat(retrievedAppointment.getDiagnosis()).isNotBlank().isEqualTo(appointment.getDiagnosis());
		assertThat(retrievedAppointment.getTreatmentPlan()).isNotBlank().isEqualTo(appointment.getTreatmentPlan());
		assertThat(retrievedAppointment.getDataKey()).isNotBlank();
	}

	private Appointment createAppointment() {
		var appointment = new Appointment();
		appointment.setId(UUID.randomUUID());
		appointment.setDiagnosis(RandomString.make());
		appointment.setTreatmentPlan(RandomString.make());
		return appointment;
	}

}

@Getter
@Setter
@Entity
@Table(name = "appointments")
@EntityListeners(FieldEncryptionListener.class)
class Appointment {

	@Id
	private UUID id;

	@Encryptable
	private String diagnosis;

	@Encryptable
	private String treatmentPlan;

	@EncryptedDataKey
	@Setter(AccessLevel.NONE)
	private String dataKey;

}

@Repository
interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

}
