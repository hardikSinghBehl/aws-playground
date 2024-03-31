package com.behl.cipherinator.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.behl.cipherinator.entity.MedicalRecord;

import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;

@Repository
public class MedicalRecordRepository {

	private final DynamoDbTable<MedicalRecord> medicalRecordTable;

	public MedicalRecordRepository(DynamoDbEnhancedClient dynamoDbClient) {
		this.medicalRecordTable = dynamoDbClient.table(MedicalRecord.TABLE_NAME, TableSchema.fromBean(MedicalRecord.class));
	}

	public String save(@NonNull final MedicalRecord medicalRecord) {
		var request = UpdateItemEnhancedRequest.builder(MedicalRecord.class).item(medicalRecord).build();
		var response = medicalRecordTable.updateItemWithResponse(request);
		return response.attributes().getId();
	}

	public Optional<MedicalRecord> findById(@NonNull final String medicalRecordId) {
		var key = Key.builder().partitionValue(medicalRecordId).build();
		var medicalRecord = medicalRecordTable.getItem(key);
		return Optional.ofNullable(medicalRecord);
	}

}
