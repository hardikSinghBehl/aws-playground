package com.behl.cipherinator.utility;

import java.lang.reflect.Field;

import org.springframework.stereotype.Component;

import com.behl.cipherinator.annotation.Encryptable;
import com.behl.cipherinator.service.EnvelopeEncryptionService.Decryptor;
import com.behl.cipherinator.service.EnvelopeEncryptionService.Encryptor;

import lombok.NonNull;
import lombok.SneakyThrows;

/**
 * Component responsible for managing the encryption and decryption of fields in
 * an object. The desired operation is performed on {@code String} fields
 * annotated with {@link Encryptable}.
 */
@Component
public class FieldEncryptionManager {

	/**
	 * Encrypts all {@link Encryptable} annotated fields of the given object.
	 * 
	 * @param target The object whose fields are to be encrypted.
	 * @param encryptor The encryptor instance to use for encryption.
	 * @throws IllegalArgumentException if any of the provided argument is {@code null}
	 */
	@SneakyThrows(IllegalAccessException.class)
	public void encryptFields(@NonNull Object target, @NonNull Encryptor encryptor) {
		Class<?> clazz = target.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Encryptable.class)) {
				field.setAccessible(true);
				String originalValue = String.valueOf(field.get(target));
				String encryptedValue = encryptor.encrypt(originalValue);
				field.set(target, encryptedValue);
			}
		}
	}

	/**
	 * Decrypts all {@link Encryptable} annotated fields of the given object.
	 * 
	 * @param target The object whose fields are to be decrypted.
	 * @param decryptor The decryptor instance to use for decryption.
	 * @throws IllegalArgumentException if any of the provided argument is {@code null}
	 */
	@SneakyThrows(IllegalAccessException.class)
	public void decryptFields(@NonNull Object target, @NonNull Decryptor decryptor) {
		Class<?> clazz = target.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Encryptable.class)) {
				field.setAccessible(true);
				String encryptedValue = String.valueOf(field.get(target));
				String decryptedValue = decryptor.decrypt(encryptedValue);
				field.set(target, decryptedValue);
			}
		}
	}

}
