package com.behl.cipherinator.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "MedicalRecords")
public class MedicalRecord {

	@DynamoDBHashKey
	@DynamoDBAttribute(attributeName = "Id")
	private String id;

	@DynamoDBAttribute(attributeName = "PatientName")
	private String patientName;

	@DynamoDBAttribute(attributeName = "MedicalHistory")
	private String medicalHistory;

	@DynamoDBAttribute(attributeName = "Diagnosis")
	private String diagnosis;

	@DynamoDBAttribute(attributeName = "TreatmentPlan")
	private String treatmentPlan;

	@DynamoDBAttribute(attributeName = "Allergies")
	private String allergies;

	@DynamoDBAttribute(attributeName = "AttendingPhysicianName")
	private String attendingPhysicianName;

	@DynamoDBAttribute(attributeName = "EncryptedDataKey")
	private String encryptedDataKey;

}
