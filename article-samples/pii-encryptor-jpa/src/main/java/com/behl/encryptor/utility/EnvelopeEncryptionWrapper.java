package com.behl.encryptor.utility;

import java.nio.ByteBuffer;
import java.util.Base64;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DataKeySpec;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.behl.encryptor.configuration.AwsConfigurationProperties;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Utility class for performing envelope encryption operations using AWS Key
 * Management Service (KMS). Manages the generation and retrieval of data
 * encryption keys (DEKs) and provides instances for encryption and decryption
 * operations.
 * 
 * @see com.behl.encryptor.configuration.AwsConfiguration
 * @see com.behl.encryptor.configuration.AwsConfigurationProperties
 */
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AwsConfigurationProperties.class)
public class EnvelopeEncryptionWrapper {

	private final AWSKMS awsKms;
	private final AwsConfigurationProperties awsConfigurationProperties;
	
	/**
	 * Creates and returns an Encryptor instance for performing encryption
	 * operation. The data encryption key (DEK) is generated using AWS KMS, which is
	 * used when initializing the Encryptor class.
	 * 
	 * The Encryptor can be used to encrypt multiple data fields using the same DEK,
	 * minimizing the need for repeated calls to AWS KMS and reducing overall
	 * latency. The encrypted data key corresponding to the current Encryptor
	 * instance is required to be stored with the encrypted data field(s) in a
	 * datastore to facilitate decryption.
	 * 
	 * @return Encryptor An instance of the {@link Encryptor} class.
	 */
	public Encryptor getEncryptor() {
		var keyId = awsConfigurationProperties.getKms().getKeyId();
		var generateDataKeyRequest = new GenerateDataKeyRequest().withKeyId(keyId).withKeySpec(DataKeySpec.AES_256);
		var dataKeyResult = awsKms.generateDataKey(generateDataKeyRequest);
		
		var dataEncryptionKey = dataKeyResult.getPlaintext().array();
		var encryptedDataKey = dataKeyResult.getCiphertextBlob().array();

		return new Encryptor(dataEncryptionKey, encryptedDataKey);
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
	 * @return Decryptor An instance of the {@link Decryptor} class.
	 * @throws IllegalArgumentException if the encryptedDataKey is {@code null}.
	 */
	public Decryptor getDecryptor(@NonNull String encryptedDataKey) {
		var decodedEncryptedDataKey = Base64.getDecoder().decode(encryptedDataKey);
		
		var decryptRequest = new DecryptRequest().withCiphertextBlob(ByteBuffer.wrap(decodedEncryptedDataKey));
		var decryptResult = awsKms.decrypt(decryptRequest);
		var dataEncryptionKey = decryptResult.getPlaintext().array();
		
		return new Decryptor(dataEncryptionKey);
	}

}
