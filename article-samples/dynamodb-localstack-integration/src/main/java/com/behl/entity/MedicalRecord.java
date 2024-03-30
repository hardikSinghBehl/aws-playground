package com.behl.entity;

import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Setter
@DynamoDbBean
public class MedicalRecord {

	public static final String TABLE_NAME = "MedicalRecords";

	private String id;

	private String patientName;

	private String diagnosis;

	private String treatmentPlan;

	private String attendingPhysicianName;

	@DynamoDbPartitionKey
	@DynamoDbAttribute("Id")
	public String getId() {
		return id;
	}

	@DynamoDbAttribute("PatientName")
	public String getPatientName() {
		return patientName;
	}

	@DynamoDbAttribute("Diagnosis")
	public String getDiagnosis() {
		return diagnosis;
	}

	@DynamoDbAttribute("TreatmentPlan")
	public String getTreatmentPlan() {
		return treatmentPlan;
	}

	@DynamoDbAttribute("AttendingPhysicianName")
	public String getAttendingPhysicianName() {
		return attendingPhysicianName;
	}

}