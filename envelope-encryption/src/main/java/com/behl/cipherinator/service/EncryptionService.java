package com.behl.cipherinator.service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DataKeySpec;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;
import com.behl.cipherinator.dto.EncryptionResultDto;
import com.behl.cipherinator.properties.AwsKmsConfigurationProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AwsKmsConfigurationProperties.class)
public class EncryptionService {

	private final AWSKMS awsKms;
	private final AwsKmsConfigurationProperties awsKmsConfigurationProperties;

	public EncryptionResultDto encrypt(final String password) {
		final String ENCRYPTION_ALGORITHM = awsKmsConfigurationProperties.getKms().getEncryptionAlgorithm();
		final String ENCODING_TYPE = awsKmsConfigurationProperties.getKms().getEncodingType();

		final GenerateDataKeyRequest generateDataKeyRequest = new GenerateDataKeyRequest()
				.withKeyId(awsKmsConfigurationProperties.getKms().getKeyId()).withKeySpec(DataKeySpec.AES_256);
		GenerateDataKeyResult dataKeyResult = awsKms.generateDataKey(generateDataKeyRequest);
		String encryptedData;
		try {
			Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE,
					new SecretKeySpec(dataKeyResult.getPlaintext().array(), ENCRYPTION_ALGORITHM));
			dataKeyResult.getPlaintext().clear();
			encryptedData = Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes()));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException exception) {
			log.error("Unable to encrypt provided password", exception);
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
		try {
			return EncryptionResultDto.builder().encryptedResult(encryptedData)
					.encryptedDataKey(new String(dataKeyResult.getCiphertextBlob().array(), ENCODING_TYPE)).build();
		} catch (UnsupportedEncodingException exception) {
			log.error("Unable to encode encrypted data key", exception);
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
	}

	public String decrypt(final String encryptedPassword, final String cipherDataKey) {
		final String ENCRYPTION_ALGORITHM = awsKmsConfigurationProperties.getKms().getEncryptionAlgorithm();
		final String ENCODING_TYPE = awsKmsConfigurationProperties.getKms().getEncodingType();
		DecryptRequest decryptRequest;
		try {
			decryptRequest = new DecryptRequest()
					.withCiphertextBlob(ByteBuffer.wrap(cipherDataKey.getBytes(ENCODING_TYPE)));
		} catch (final UnsupportedEncodingException exception) {
			log.error("Exception occured during DecryptRequest.class instance construction", exception);
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
		DecryptResult decryptResult = awsKms.decrypt(decryptRequest);
		byte[] decodeBase64src = Base64.getDecoder().decode(encryptedPassword);
		try {
			Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE,
					new SecretKeySpec(decryptResult.getPlaintext().array(), ENCRYPTION_ALGORITHM));
			decryptResult.getPlaintext().clear();
			return new String(cipher.doFinal(decodeBase64src));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException exception) {
			log.error("Unable to decrypt encrypted cipher with data key", exception);
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
	}

}
