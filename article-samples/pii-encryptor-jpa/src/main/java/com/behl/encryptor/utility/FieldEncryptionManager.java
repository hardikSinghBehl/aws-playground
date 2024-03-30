package com.behl.encryptor.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.behl.encryptor.annotation.Encryptable;
import com.behl.encryptor.annotation.EncryptedDataKey;
import com.behl.encryptor.exception.InvalidEncryptedDataKeyTypeException;
import com.behl.encryptor.exception.MissingEncryptableFieldsException;
import com.behl.encryptor.exception.MultipleEncryptedDataKeysFoundException;
import com.behl.encryptor.exception.NoEncryptedDataKeyFoundException;

import jakarta.persistence.Id;
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
		
	private final EnvelopeEncryptionWrapper envelopeEncryptionWrapper;
	
	/**
	 * Encrypts all {@code String} fields annotated with {@link Encryptable} in the
	 * given object. The given target object must contain a {@code String} variable 
	 * annotated with @EncryptedDataKey, the value of which will also be set by the method.
	 * 
	 * @param target The object whose field(s) are to be encrypted.
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 */
	@SneakyThrows
	public void encryptFields(@NonNull Object target) {
		var targetClass = target.getClass();
		var encryptableFields = getEncryptableFields(targetClass);
		var encryptedDataKeyField = getEncryptedDataKeyField(targetClass);	
		
		var encryptor = envelopeEncryptionWrapper.getEncryptor();
		for (var field : encryptableFields) {
			var originalValue = String.valueOf(field.get(target));
			var encryptedValue = encryptor.encrypt(originalValue);
			field.set(target, encryptedValue);
		}
		encryptedDataKeyField.set(target, encryptor.getEncryptedDataKey());
	}

	/**
	 * Decrypts all {@code String} fields annotated with {@link Encryptable} in the
	 * given object. The given target object must contain a {@code String} variable
	 * annotated with @EncryptedDataKey, the value of which is required to decrypt
	 * fields.
	 * 
	 * @param target The object whose field(s) are to be decrypted.
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 */
	@SneakyThrows
	public void decryptFields(@NonNull Object target) {
		var targetClass = target.getClass();
		var encryptableFields = getEncryptableFields(targetClass);
		var encryptedDataKeyField = getEncryptedDataKeyField(targetClass);
		var encryptedDataKey = String.valueOf(encryptedDataKeyField.get(target));
		
		var decryptor = envelopeEncryptionWrapper.getDecryptor(encryptedDataKey);
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
				var isPrimaryKey = field.isAnnotationPresent(Id.class);
				if (Boolean.TRUE.equals(isString) && Boolean.FALSE.equals(isEncryptedDataKey)
						&& Boolean.FALSE.equals(isPrimaryKey)) {
					field.setAccessible(Boolean.TRUE);
					encryptableFields.add(field);	
				}
			}
		}
		if (Boolean.TRUE.equals(encryptableFields.isEmpty())) {
			throw new MissingEncryptableFieldsException("No valid field annotated with @Encryptable found in the target class.");
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
	@SneakyThrows
	private Field getEncryptedDataKeyField(@NonNull Class<?> targetClass) {
		var fields = Arrays.stream(targetClass.getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(EncryptedDataKey.class))
				.peek(field -> field.setAccessible(Boolean.TRUE))
				.toList();
		
		if (fields.isEmpty()) {
			throw new NoEncryptedDataKeyFoundException("No field annotated with @EncryptedDataKey found in the target class.");
		} else if (fields.size() > 1) {
			throw new MultipleEncryptedDataKeysFoundException("More than one field annotated with @EncryptedDataKey found in the target class.");
		} else if (Boolean.FALSE.equals(fields.get(0).getType().isAssignableFrom(String.class))) {
			throw new InvalidEncryptedDataKeyTypeException("Field annotated with @EncryptedDataKey must be of type String");
		} else {
			return fields.get(0);
		}
	}
	
}
