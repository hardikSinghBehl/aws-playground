package com.behl.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.behl.entity.MedicalRecord;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MedicalRecordRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public void save(@NonNull MedicalRecord medicalRecord) {
		dynamoDBMapper.save(medicalRecord);
	}

	public Optional<MedicalRecord> findById(@NonNull String medicalRecordId) {
		var medicalRecord = dynamoDBMapper.load(MedicalRecord.class, medicalRecordId);
		return Optional.ofNullable(medicalRecord);
	}

}
