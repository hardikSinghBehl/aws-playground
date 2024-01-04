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
import com.behl.cipherinator.dto.EncryptionResultDto;

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
	 * Encrypts the provided data using envelope encryption. A data encryption key
	 * (DEK) is generated using AWS KMS, the data is encrypted with the DEK using
	 * the AES algorithm, and returns the encrypted data along with encrypted data
	 * key both of which are Base64-encoded.
	 *
	 * @param data The data to be encrypted.
	 * @return {@link EncryptionResultDto} containing the encrypted data and the
	 *         encrypted data key.
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 */
	public EncryptionResultDto encrypt(@NonNull final String data) {
		final var keyId = awsConfigurationProperties.getKms().getKeyId();
		final var generateDataKeyRequest = new GenerateDataKeyRequest().withKeyId(keyId).withKeySpec(DataKeySpec.AES_256);
		final var dataKeyResult = awsKms.generateDataKey(generateDataKeyRequest);
		
		final var encryptedData = performAESCipherOperation(Cipher.ENCRYPT_MODE, dataKeyResult.getPlaintext().array(), data.getBytes());
		dataKeyResult.getPlaintext().clear();
		
		final var encoder = Base64.getEncoder();
		final var encodedEncryptedData = encoder.encodeToString(encryptedData);
		final var encryptedDataKey = encoder.encodeToString(dataKeyResult.getCiphertextBlob().array());

		return EncryptionResultDto.builder()
				.encryptedData(encodedEncryptedData)
				.encryptedDataKey(encryptedDataKey)
				.build();
	}

	/**
	 * Decrypts the provided encrypted data using envelope encryption. Uses the
	 * encrypted data key to obtain the data encryption key (DEK) from AWS KMS, and
	 * then uses the DEK to decrypt the data using the AES algorithm.
	 *
	 * @param encryptedData The Base64-encoded data to be decrypted.
	 * @param encryptedDataKey The Base64-encoded encrypted data key
	 * @return The decrypted data as a String.
	 * @throws IllegalArgumentException if any provided argument is {@code null}
	 */
	public String decrypt(@NonNull final String encryptedData, @NonNull final String encryptedDataKey) {
		final var decoder = Base64.getDecoder();
		final var decodedEncryptedDataKey = decoder.decode(encryptedDataKey);
		final var decodedEncryptedData = decoder.decode(encryptedData);

		final var decryptRequest = new DecryptRequest().withCiphertextBlob(ByteBuffer.wrap(decodedEncryptedDataKey));
		final var decryptResult = awsKms.decrypt(decryptRequest);

		final var decryptedData = performAESCipherOperation(Cipher.DECRYPT_MODE, decryptResult.getPlaintext().array(), decodedEncryptedData);
		decryptResult.getPlaintext().clear();

		return new String(decryptedData);
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
