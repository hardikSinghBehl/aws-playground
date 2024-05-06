package com.behl.cipherinator.utility;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import lombok.SneakyThrows;

public class AESUtils {
	
	private static final String AES_ALGORITHM = "AES";

	/**
	 * Performs encryption/decryption using AES Algorithm on the input data using
	 * the provided key.
	 * 
	 * @param mode  The cipher mode, either {@link Cipher#ENCRYPT_MODE} or
	 *              {@link Cipher#DECRYPT_MODE} signifying the operation to be
	 *              performed
	 * @param key   The key to be used for performing AES cipher operation.
	 * @param input The input data to be processed.
	 * @return The result of the AES encryption or decryption operation.
	 */
	@SneakyThrows
	public static byte[] performCipherOperation(int mode, byte[] key, byte[] input) {
		var cipher = Cipher.getInstance(AES_ALGORITHM);
		var secretKey = new SecretKeySpec(key, AES_ALGORITHM);
		cipher.init(mode, secretKey);
		return cipher.doFinal(input);
	}

}
