package com.behl.cipherinator.utility;

import java.nio.ByteBuffer;
import java.util.Base64;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.behl.cipherinator.configuration.AwsConfiguration;
import com.behl.cipherinator.configuration.AwsConfigurationProperties;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DataKeySpec;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.GenerateDataKeyRequest;

/**
 * Component for performing envelope encryption operations using AWS Key
 * Management Service (KMS). Manages the generation and retrieval of data
 * encryption keys (DEKs) and the encryption and decryption of data using the
 * AES algorithm.
 * 
 * @see AwsConfiguration
 * @see AwsConfigurationProperties
 */
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AwsConfigurationProperties.class)
public class EnvelopeEncryptionManager {

	private final KmsClient kmsClient;
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
	 * @return Encryptor An instance of the Encryptor class.
	 */
	public Encryptor getEncryptor() {
		var keyId = awsConfigurationProperties.getKms().getKeyId();
		var generateDataKeyRequest = GenerateDataKeyRequest.builder().keyId(keyId).keySpec(DataKeySpec.AES_256).build();
		var dataKeyResult = kmsClient.generateDataKey(generateDataKeyRequest);
		
		var dataEncryptionKey = dataKeyResult.plaintext().asByteArray();
		var encryptedDataKey = dataKeyResult.ciphertextBlob().asByteArray();

		return new Encryptor(dataEncryptionKey, encryptedDataKey);
	}
	
	/**
	 * Returns an existing Encryptor instance for performing encryption operation
	 * corresponding to given encrypted data key. The same data encryption key (DEK)
	 * will be returned which was produced when given Encryptor was initially created.
	 * 
	 * This method is intended to be leveraged when an existing record with encrypted
	 * is to be updated in the database and creation of a new data key pair is 
	 * not needed.
	 * 
	 * @return Encryptor An instance of the Encryptor class.
	 */
	public Encryptor getEncryptor(@NonNull final String encryptedDataKey) {
		var decoder = Base64.getDecoder();
		var decodedEncryptedDataKey = decoder.decode(encryptedDataKey);
		var cipherTextBlob = SdkBytes.fromByteBuffer(ByteBuffer.wrap(decodedEncryptedDataKey));
		
		var decryptRequest = DecryptRequest.builder().ciphertextBlob(cipherTextBlob).build();
		var decryptResult = kmsClient.decrypt(decryptRequest);
		var dataEncryptionKey = decryptResult.plaintext().asByteArray();

		return new Encryptor(dataEncryptionKey, decodedEncryptedDataKey);
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
		var decoder = Base64.getDecoder();
		var decodedEncryptedDataKey = decoder.decode(encryptedDataKey);
		var cipherTextBlob = SdkBytes.fromByteBuffer(ByteBuffer.wrap(decodedEncryptedDataKey));
		
		var decryptRequest = DecryptRequest.builder().ciphertextBlob(cipherTextBlob).build();
		var decryptResult = kmsClient.decrypt(decryptRequest);
		var dataEncryptionKey = decryptResult.plaintext().asByteArray();
		
		return new Decryptor(dataEncryptionKey);
	}

}
