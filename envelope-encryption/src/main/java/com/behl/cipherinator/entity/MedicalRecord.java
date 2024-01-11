package com.behl.cipherinator.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.behl.cipherinator.utility.Encryptable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "MedicalRecords")
public class MedicalRecord {

	@DynamoDBHashKey
	@DynamoDBAttribute(attributeName = "Id")
	private String id;

	@Encryptable
	@DynamoDBAttribute(attributeName = "PatientName")
	private String patientName;

	@Encryptable
	@DynamoDBAttribute(attributeName = "MedicalHistory")
	private String medicalHistory;

	@Encryptable
	@DynamoDBAttribute(attributeName = "Diagnosis")
	private String diagnosis;

	@Encryptable
	@DynamoDBAttribute(attributeName = "TreatmentPlan")
	private String treatmentPlan;

	@Encryptable
	@DynamoDBAttribute(attributeName = "Allergies")
	private String allergies;

	@DynamoDBAttribute(attributeName = "AttendingPhysicianName")
	private String attendingPhysicianName;

	@DynamoDBAttribute(attributeName = "EncryptedDataKey")
	private String encryptedDataKey;

}
