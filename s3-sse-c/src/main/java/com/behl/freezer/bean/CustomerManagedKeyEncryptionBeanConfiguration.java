package com.behl.freezer.bean;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.services.s3.model.SSECustomerKey;
import com.behl.freezer.properties.SecretKeyConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Hardik Singh Behl
 * @email behl.hardiksingh@gmail.com
 */

@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(value = SecretKeyConfigurationProperties.class)
public class CustomerManagedKeyEncryptionBeanConfiguration {

	private final SecretKeyConfigurationProperties secretKeyConfigurationProperties;

	@Bean
	@Primary
	public SSECustomerKey sseCustomerKey() {
		return new SSECustomerKey(getSecretKey());
	}

	private SecretKey getSecretKey() {
		final int iterationCount = 65536;
		final int keyLength = 256;
		SecretKeyFactory factory;
		final var secretKeyConfig = secretKeyConfigurationProperties.getSecretKey();

		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		} catch (final NoSuchAlgorithmException exception) {
			log.error("Unable to get instance for configured algorithm", exception);
			throw new RuntimeException(exception);
		}
		KeySpec keySpec = new PBEKeySpec(secretKeyConfig.getValue().toCharArray(), secretKeyConfig.getSalt().getBytes(),
				iterationCount, keyLength);
		SecretKey secretKey;
		try {
			secretKey = new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), "AES");
		} catch (final InvalidKeySpecException exception) {
			log.error("Unable to create secret key with configured specifications", exception);
			throw new RuntimeException(exception);
		}
		return secretKey;
	}

}
