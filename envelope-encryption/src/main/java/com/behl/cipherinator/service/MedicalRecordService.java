package com.behl.cipherinator.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.behl.cipherinator.dto.MedicalRecordCreationDto;
import com.behl.cipherinator.dto.MedicalRecordDto;
import com.behl.cipherinator.entity.MedicalRecord;
import com.behl.cipherinator.exception.InvalidMedicalRecordIdException;
import com.behl.cipherinator.repository.MedicalRecordRepository;
import com.behl.cipherinator.utility.FieldEncryptionManager;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

	private final FieldEncryptionManager fieldEncryptionManager;
	private final MedicalRecordRepository medicalRecordRepository;
	private final EnvelopeEncryptionService envelopeEncryptionService;
	
	private final ModelMapper modelMapper = new ModelMapper();

	/**
	 * Creates a new patient medical record in the DataSource corresponding to
	 * provided request.
	 * 
	 * Sensitive fields (PII and PHI data) is encrypted before being saved in the
	 * datasource.
	 * 
	 * @return The ID of the newly created medical record.
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 */
	public String create(@NonNull final MedicalRecordCreationDto medicalRecordCreationRequest) {
		final var encryptor = envelopeEncryptionService.getEncryptor();
		
		final var medicalRecord = modelMapper.map(medicalRecordCreationRequest, MedicalRecord.class);
		fieldEncryptionManager.encryptFields(medicalRecord, encryptor);
		
		medicalRecord.setId(UUID.randomUUID().toString());
		medicalRecord.setEncryptedDataKey(encryptor.getEncryptedDataKey());

		medicalRecordRepository.save(medicalRecord);
		return medicalRecord.getId();
	}
	
	/**
	 * Retrieves medical record corresponding to provided medical record ID.
	 * Sensitive fields are decrypted back to their original form before being 
	 * returned.
	 * 
	 * @return DTO representing retrieved medical record with decrypted data.
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 * @throws InvalidMedicalRecordIdException if no medical record exists with provided Id
	 */
	public MedicalRecordDto retrieve(@NonNull final String medicalRecordId) {
		final var medicalRecord = medicalRecordRepository.findById(medicalRecordId).orElseThrow(InvalidMedicalRecordIdException::new);
		final var decryptor = envelopeEncryptionService.getDecryptor(medicalRecord.getEncryptedDataKey());
		
		fieldEncryptionManager.decryptFields(medicalRecord, decryptor);
		
		return modelMapper.map(medicalRecord, MedicalRecordDto.class);
	}

}
