package com.behl.encryptor.utility;

import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Utility class for encrypting data using a data encryption key (DEK) and
 * providing the corresponding encrypted data key.
 */
@RequiredArgsConstructor
public class Encryptor {
	
	private static final Encoder ENCODER = Base64.getEncoder();

	private final byte[] dataEncryptionKey;
	private final byte[] encryptedDataKey;
	
	/**
	 * Encrypts the provided data using the DEK corresponding to the current
	 * Encryptor instance.
	 *
	 * @param data The data to be encrypted.
	 * @return The Base64-encoded encrypted data.
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 */
	public String encrypt(@NonNull String data) {
		final var encryptedData = AESUtils.performCipherOperation(Cipher.ENCRYPT_MODE, dataEncryptionKey, data.getBytes());
		return ENCODER.encodeToString(encryptedData);
	}

	/**
	 * Returns the encrypted DEK corresponding to the current Encryptor instance.
	 * This encrypted key should be stored alongside the encrypted data for future
	 * decryption operation.
	 *
	 * @return The Base64-encoded encrypted DEK.
	 */
	public String getEncryptedDataKey() {
		return ENCODER.encodeToString(encryptedDataKey);
	}

}