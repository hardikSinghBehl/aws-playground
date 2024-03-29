package com.behl.encryptor.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(KmsProperties.PREFIX)
public class KmsProperties {
	
	public static final String PREFIX = "aws.kms";

	/**
	 * <p>
	 * The KeyID corresponding to provisioned AWS KMS Key. This property corresponds
	 * to the key {@code aws.kms.key-id} in the active .yml configuration file.
	 * </p>
	 *
	 * <p>
	 * <b> Note: </b>
	 * <ul>
	 * <li>The configured KMS key should be a Symmetric Key</li>
	 * <li>The value can be either the KeyID or the Key ARN.</li>
	 * </ul>
	 * </p>
	 */
	private String keyId;


}