package com.behl.cipherinator.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.behl.aws")
public class AwsKmsConfigurationProperties {

	private KMS kms = new KMS();

	@Data
	public class KMS {
		/**
		 * KeyID of KMS Symmetric Key that will be used to generate data key pairs
		 * https://docs.aws.amazon.com/kms/latest/developerguide/create-keys.html
		 */
		private String keyId;

		private String encryptionAlgorithm;
		private String encodingType;
	}

}