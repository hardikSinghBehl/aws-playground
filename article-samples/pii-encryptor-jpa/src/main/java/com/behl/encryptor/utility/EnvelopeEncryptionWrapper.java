package com.behl.encryptor.utility;

import java.nio.ByteBuffer;
import java.util.Base64;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.behl.encryptor.configuration.KmsProperties;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DataKeySpec;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.GenerateDataKeyRequest;

/**
 * Utility class for performing envelope encryption operations using AWS Key
 * Management Service (KMS). Manages the generation and retrieval of data
 * encryption keys (DEKs) and provides instances for encryption and decryption
 * operations.
 * 
 * @see com.behl.encryptor.configuration.KmsProperties
 */
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(value = KmsProperties.class)
public class EnvelopeEncryptionWrapper {

	private final KmsClient kmsClient;
	private final KmsProperties kmsProperties;
	
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
		var keyId = kmsProperties.getKeyId();
		var generateDataKeyRequest = GenerateDataKeyRequest.builder().keyId(keyId).keySpec(DataKeySpec.AES_256).build();
		var dataKeyResult = kmsClient.generateDataKey(generateDataKeyRequest);
		
		var dataEncryptionKey = dataKeyResult.plaintext().asByteArray();
		var encryptedDataKey = dataKeyResult.ciphertextBlob().asByteArray();

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
		var cipherTextBlob = SdkBytes.fromByteBuffer(ByteBuffer.wrap(decodedEncryptedDataKey));
		
		var decryptRequest = DecryptRequest.builder().ciphertextBlob(cipherTextBlob).build();
		var decryptResult = kmsClient.decrypt(decryptRequest);
		var dataEncryptionKey = decryptResult.plaintext().asByteArray();
		
		return new Decryptor(dataEncryptionKey);
	}

}
