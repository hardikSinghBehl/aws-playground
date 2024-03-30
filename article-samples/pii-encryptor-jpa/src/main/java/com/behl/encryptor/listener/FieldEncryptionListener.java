package com.behl.encryptor.listener;

import org.springframework.stereotype.Component;

import com.behl.encryptor.utility.FieldEncryptionManager;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
	@PreUpdate
	private void beforeSave(Object object) {
		fieldEncryptionManager.encryptFields(object);
	}

	@PostLoad
	private void afterLoad(Object object) {
		fieldEncryptionManager.decryptFields(object);
	}

}