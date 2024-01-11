package com.behl.cipherinator.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "MedicalRecordCreation", accessMode = Schema.AccessMode.READ_ONLY)
public class MedicalRecordCreationDto {

	@NotBlank(message = "PatientName must not be empty.")
	@Size(min = 3, max = 20, message = "PatientName length must be between 3 and 20 characters.")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Patient's name for whom the medical record is created.", example = "John Doe", minLength = 3, maxLength = 20)
	private String patientName;

	@NotBlank(message = "MedicalHistory must not be empty.")
	@Size(max = 100, message = "MedicalHistory length must not exceed 100 characters.")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Comprehensive medical history details of the patient.", example = "History of Type II Diabetes; Managed hypertension.", maxLength = 100)
	private String medicalHistory;

	@NotBlank(message = "Diagnosis must not be empty.")
	@Size(max = 100, message = "Diagnosis length must not exceed 100 characters.")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Medical diagnosis information for the patient.", example = "Type II Diabetes; Hypertension", maxLength = 100)
	private String diagnosis;

	@NotBlank(message = "TreatmentPlan must not be empty.")
	@Size(max = 100, message = "TreatmentPlan length must not exceed 100 characters.")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Information about the patient's treatment plan.", example = "Metformin 500mg daily; Lisinopril 10mg daily; Regular exercise.", maxLength = 100)
	private String treatmentPlan;

	@NotBlank(message = "Allergies must not be empty.")
	@Size(max = 100, message = "Allergies length must not exceed 100 characters.")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Details of any patient allergies.", example = "Penicillin", maxLength = 100)
	private String allergies;

	@NotBlank(message = "AttendingPhysicianName must not be empty")
	@Size(min = 3, max = 20, message = "AttendingPhysicianName length must be between 3 and 20 characters")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Identifying name of the physician treating the patient.", example = "Dr. Doofenshmirtz", minLength = 3, maxLength = 20)
	private String attendingPhysicianName;

}
