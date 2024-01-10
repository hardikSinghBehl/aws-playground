package com.behl.cipherinator.service;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DataKeySpec;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.behl.cipherinator.configuration.AwsConfiguration;
import com.behl.cipherinator.configuration.AwsConfigurationProperties;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Service class for performing envelope encryption operations using AWS Key
 * Management Service (KMS). Manages the generation and retrieval of data
 * encryption keys (DEKs) and the encryption and decryption of data using the
 * AES algorithm.
 * 
 * @see AwsConfiguration
 * @see AwsConfigurationProperties
 */
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AwsConfigurationProperties.class)
public class EnvelopeEncryptionService {

	private final AWSKMS awsKms;
	private final AwsConfigurationProperties awsConfigurationProperties;
	
	/**
	 * Creates and returns an Encryptor instance for performing encryption
	 * operation. The data encryption key (DEK) is generated using AWS KMS, which is
	 * used when initializing the Encryptor class.
	 * 
	 * The Encryptor can be used to encrypt multiple data fields using the same DEK,
	 * minimizing the need for repeated calls to AWS KMS and reducing overall
	 * latency. The encrypted data key corresponding to the current Encryptor instance is required to be stored with the encrypted
	 * data field(s) in a datastore to facilitate decryption.
	 * 
	 * @return Encryptor An instance of the Encryptor class.
	 */
	public Encryptor getEncryptor() {
		final var keyId = awsConfigurationProperties.getKms().getKeyId();
		final var generateDataKeyRequest = new GenerateDataKeyRequest().withKeyId(keyId).withKeySpec(DataKeySpec.AES_256);
		final var dataKeyResult = awsKms.generateDataKey(generateDataKeyRequest);
		
		final var dataEncryptionKey = dataKeyResult.getPlaintext().array();
		final var encryptedDataKey = dataKeyResult.getCiphertextBlob().array();

		return new Encryptor(dataEncryptionKey, encryptedDataKey);
	}
	
	@RequiredArgsConstructor
	public class Encryptor {

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
		public String encrypt(@NonNull final String data) {
			final var encryptedData = performAESCipherOperation(Cipher.ENCRYPT_MODE, dataEncryptionKey,	data.getBytes());
			return Base64.getEncoder().encodeToString(encryptedData);
		}

		/**
		 * Returns the encrypted DEK corresponding to the current Encryptor instance.
		 * This encrypted key should be stored alongside the encrypted data for future
		 * decryption operation.
		 *
		 * @return The Base64-encoded encrypted DEK.
		 */
		public String getEncryptedDataKey() {
			final var encoder = Base64.getEncoder();
			return encoder.encodeToString(encryptedDataKey);
		}

	}
	
	/**
	 * Creates and returns a Decryptor instance for decrypting data. The encrypted
	 * data key is used to obtain the plaintext data encryption key (DEK) from AWS
	 * KMS, which is used when initializing the Decryptor class.
	 *
	 * The Decryptor can be used to decrypt multiple data fields using the same DEK,
	 * minimizing the need for repeated calls to AWS KMS and reducing overall
	 * latency.
	 *
	 * @param encryptedDataKey The Base64-encoded encrypted data key
	 * @return Decryptor An instance of the Decryptor class.
	 * @throws IllegalArgumentException if the encryptedDataKey is {@code null}.
	 */
	public Decryptor getDecryptor(@NonNull final String encryptedDataKey) {
		final var decoder = Base64.getDecoder();
		final var decodedEncryptedDataKey = decoder.decode(encryptedDataKey);
		
		final var decryptRequest = new DecryptRequest().withCiphertextBlob(ByteBuffer.wrap(decodedEncryptedDataKey));
		final var decryptResult = awsKms.decrypt(decryptRequest);
		final var dataEncryptionKey = decryptResult.getPlaintext().array();
		
		return new Decryptor(dataEncryptionKey);
	}
	
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
			final var decoder = Base64.getDecoder();
			final var decodedEncryptedData = decoder.decode(encryptedData);
			
			final var decryptedData = performAESCipherOperation(Cipher.DECRYPT_MODE, dataEncryptionKey, decodedEncryptedData);
			return new String(decryptedData);
		}
		
	}
	
	/**
	 * Performs AES cipher operation (encryption or decryption) on the input data
	 * using the provided key. The cipher mode, either {@link Cipher#ENCRYPT_MODE}
	 * or {@link Cipher#DECRYPT_MODE} can be used to control the operation to be
	 * performed on the {@code input} by provided {@code key}
	 */
	@SneakyThrows(GeneralSecurityException.class)
	private byte[] performAESCipherOperation(final int mode, final byte[] key, final byte[] input) {
		final var cipher = Cipher.getInstance("AES");
		final var secretKey = new SecretKeySpec(key, "AES");
		cipher.init(mode, secretKey);
		return cipher.doFinal(input);
	}

}
