package com.behl.encryptor.entity.listener;

import org.springframework.stereotype.Component;

import com.behl.encryptor.utility.FieldEncryptionManager;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;

/**
 * Listener component responsible for encrypting and decrypting fields in
 * entities before persistence and after loading from the database,
 * respectively.
 */
@Component
@RequiredArgsConstructor
public class FieldEncryptionListener {

	private final FieldEncryptionManager fieldEncryptionManager;
	
	@PrePersist
	private void beforeUpdation(Object object) {
		fieldEncryptionManager.encryptFields(object);
	}

	@PostLoad
	private void afterLoad(Object object) {
		fieldEncryptionManager.decryptFields(object);
	}

}