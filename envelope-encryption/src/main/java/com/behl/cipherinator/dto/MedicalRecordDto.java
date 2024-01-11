package com.behl.cipherinator.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "MedicalRecord", accessMode = Schema.AccessMode.WRITE_ONLY)
public class MedicalRecordDto {

	private String id;
	private String patientName;
	private String medicalHistory;
	private String diagnosis;
	private String treatmentPlan;
	private String allergies;
	private String attendingPhysicianName;

}
