package com.behl.encryptor.utility;

import java.util.Base64;

import javax.crypto.Cipher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Utility class for decrypting data using a plaintext data encryption key
 * (DEK).
 */
@RequiredArgsConstructor
public class Decryptor {
	
	private final byte[] dataEncryptionKey;
	
	/**
	 * Decrypts the provided encrypted data using the plaintext data encryption key
	 * (DEK) stored in the current Decryptor instance.
	 *
	 * This method is intended to be used multiple times with the same DEK for
	 * decrypting multiple fields, thus leveraging the same DEK without additional
	 * AWS KMS calls for each decryption operation.
	 *
	 * @param encryptedData The Base64-encoded data to be decrypted.
	 * @return The decrypted data as a String.
	 * @throws IllegalArgumentException if the encryptedData is {@code null}.
	 */
	public String decrypt(@NonNull final String encryptedData) {
		var decodedEncryptedData = Base64.getDecoder().decode(encryptedData);
		var decryptedData = AESUtils.performCipherOperation(Cipher.DECRYPT_MODE, dataEncryptionKey, decodedEncryptedData);
		return new String(decryptedData);
	}
	
}