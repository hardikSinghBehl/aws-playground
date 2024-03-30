package com.behl.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.behl.entity.MedicalRecord;

import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class MedicalRecordRepository {

	private final DynamoDbTable<MedicalRecord> medicalRecordTable;

	public MedicalRecordRepository(DynamoDbEnhancedClient dynamoDbClient) {
		this.medicalRecordTable = dynamoDbClient.table(MedicalRecord.TABLE_NAME, TableSchema.fromBean(MedicalRecord.class));
	}

	public void save(@NonNull MedicalRecord medicalRecord) {
		medicalRecordTable.putItem(medicalRecord);
	}

	public Optional<MedicalRecord> findById(@NonNull String medicalRecordId) {
		var key = Key.builder().partitionValue(medicalRecordId).build();
		var medicalRecord = medicalRecordTable.getItem(key);
		return Optional.ofNullable(medicalRecord);
	}

}
