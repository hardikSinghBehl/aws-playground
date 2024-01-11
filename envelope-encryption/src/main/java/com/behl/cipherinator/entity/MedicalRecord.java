package com.behl.cipherinator.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.behl.cipherinator.utility.Encryptable;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a patient's medical record in the Datasource.
 *
 * Fields containing sensitive personal and health information, especially
 * Protected Health Information (PHI), are marked with {@link Encryptable}. This
 * annotation indicates the need for encryption and decryption of these fields
 * using {@link com.behl.cipherinator.utility.FieldEncryptionManager} when
 * storing or retrieving them from the database.
 */
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
