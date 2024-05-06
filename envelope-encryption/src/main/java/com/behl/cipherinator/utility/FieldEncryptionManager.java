package com.behl.cipherinator.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.behl.cipherinator.annotation.Encryptable;
import com.behl.cipherinator.annotation.EncryptedDataKey;
import com.behl.cipherinator.exception.InvalidEncryptedDataKeyTypeException;
import com.behl.cipherinator.exception.MissingEncryptableFieldsException;
import com.behl.cipherinator.exception.MultipleEncryptedDataKeysFoundException;
import com.behl.cipherinator.exception.NoEncryptedDataKeyFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Component responsible for managing the encryption and decryption of fields in
 * an object. The desired operation is performed on {@code String} fields
 * annotated with {@link Encryptable}.
 */
@Component
@RequiredArgsConstructor
public class FieldEncryptionManager {
	
	private final EnvelopeEncryptionManager envelopeEncryptionManager;

	/**
	 * Encrypts all fields annotated with {@link Encryptable} in the given object.
	 * 
	 * @param target The object whose fields are to be encrypted.
	 * @throws IllegalArgumentException if any of the provided argument is {@code null}
	 */
	@SneakyThrows
	public void encryptFields(@NonNull Object target) {
		var targetClass = target.getClass();
		var encryptableFields = getEncryptableFields(targetClass);
		var encryptedDataKeyField = getEncryptedDataKeyField(targetClass);
		
		Encryptor encryptor;
		var encryptedDataKeyPresent = isPresent(encryptedDataKeyField, target);
		if (Boolean.TRUE.equals(encryptedDataKeyPresent)) {
			var encryptedDataKey = String.valueOf(encryptedDataKeyField.get(target));
			encryptor = envelopeEncryptionManager.getEncryptor(encryptedDataKey);
		} else {
			encryptor = envelopeEncryptionManager.getEncryptor();
			encryptedDataKeyField.set(target, encryptor.getEncryptedDataKey());
		}
		
		for (var field : encryptableFields) {
			var originalValue = String.valueOf(field.get(target));
			var encryptedValue = encryptor.encrypt(originalValue);
			field.set(target, encryptedValue);
		}
	}

	/**
	 * Decrypts all fields annotated with {@link Encryptable} in the given object.
	 * 
	 * @param target The object whose fields are to be decrypted.
	 * @throws IllegalArgumentException if any of the provided argument is {@code null}
	 */
	@SneakyThrows
	public void decryptFields(@NonNull Object target) {
		var targetClass = target.getClass();
		var encryptableFields = getEncryptableFields(targetClass);
		var encryptedDataKeyField = getEncryptedDataKeyField(targetClass);
		var encryptedDataKey = String.valueOf(encryptedDataKeyField.get(target));

		var decryptor = envelopeEncryptionManager.getDecryptor(encryptedDataKey);
		for (var field : encryptableFields) {
			var encryptedValue = String.valueOf(field.get(target));
			var decryptedValue = decryptor.decrypt(encryptedValue);
			field.set(target, decryptedValue);
		}
	}
	
	/**
	 * Retrieves list of all fields annotated with {@link Encryptable} annotation
	 * in the given target class. Only fields of type {@code String} are considered,
	 * the encryptedDataKey is excluded from the list even if the annotation is present.
	 * 
	 * @param targetClass The class from which to retrieve the encryptable fields.
	 * @return A list of fields annotated with {@link Encryptable}
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 */
	private List<Field> getEncryptableFields(@NonNull Class<?> targetClass) {
		var encryptableFields = new ArrayList<Field>();
		for (Field field : targetClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(Encryptable.class)) {
				var isString = field.getType().isAssignableFrom(String.class);
				var isEncryptedDataKey = field.isAnnotationPresent(EncryptedDataKey.class);
				if (Boolean.TRUE.equals(isString) && Boolean.FALSE.equals(isEncryptedDataKey)) {
					field.setAccessible(Boolean.TRUE);
					encryptableFields.add(field);	
				}
			}
		}
		if (Boolean.TRUE.equals(encryptableFields.isEmpty())) {
			throw new MissingEncryptableFieldsException();
		}
		return encryptableFields;
	}
	
	/**
	 * Retrieves the encrypted data key from the given target class.
	 * 
	 * @param targetClass The class from which to retrieve the @EncryptedDataKey field.
	 * @return the encrypted data key field from the given target class
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 */
	private Field getEncryptedDataKeyField(@NonNull Class<?> targetClass) {
		var fields = Arrays.stream(targetClass.getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(EncryptedDataKey.class))
				.peek(field -> field.setAccessible(Boolean.TRUE))
				.toList();
		
		if (fields.isEmpty()) {
			throw new NoEncryptedDataKeyFoundException();
		} else if (fields.size() > 1) {
			throw new MultipleEncryptedDataKeysFoundException();
		} else if (Boolean.FALSE.equals(fields.get(0).getType().isAssignableFrom(String.class))) {
			throw new InvalidEncryptedDataKeyTypeException();
		} else {
			return fields.get(0);
		}
	}

	/**
	 * Checks whether the value of the given field in the target object is present.
	 * 
	 * @param field The field to check for presence of value.
	 * @param target The object from which to retrieve the value of the field.
	 * @return {@code true} if the value is present, {@code false} otherwise.
	 * @throws IllegalArgumentException if any of the provided arguments is {@code null}.
	 */
	@SneakyThrows
	private boolean isPresent(@NonNull Field field, @NonNull Object target) {
		var value = String.valueOf(field.get(target));
		if (value != null && value.trim().length() > 0 && !value.equalsIgnoreCase("NULL")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}
