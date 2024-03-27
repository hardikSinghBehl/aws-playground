package com.behl.encryptor.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Maps configuration values defined in the active {@code .yml} file to the
 * corresponding variables below. The configuration properties are referenced
 * within the application to facilitate connection with AWS KMS.
 * </p>
 *
 * <p>
 * <b> Example YAML configuration: </b>
 * </p>
 * 
 * <pre>
 * com:
 *   behl:
 *     encryptor:
 *       aws:
 *         access-key: "aws-access-key"
 *         secret-access-key: "aws-secret-access-key"
 *         region: "aws-region"
 *         kms:
 *           key-id: "aws-kms-key-id"
 * </pre>
 * 
 * @see com.behl.encryptor.configuration.AwsConfiguration
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "com.behl.encryptor.aws")
public class AwsConfigurationProperties {

	/**
	 * <p>
	 * AWS access key for authenticating requests made to AWS KMS service.
	 * This property corresponds to the key {@code com.behl.encryptor.aws.access-key}
	 * in the active {@code .yml} configuration file.
	 * </p>
	 */
	@NotBlank(message = "AWS access key must be configured")
	private String accessKey;

	/**
	 * <p>
	 * AWS secret access key for authenticating requests made to AWS KMS service.
	 * This property corresponds to the key {@code com.behl.encryptor.aws.secret-access-key}
	 * in the active {@code .yml} configuration file.
	 * </p>
	 */
	@NotBlank(message = "AWS secret access key must be configured")
	private String secretAccessKey;

	/**
	 * <p>
	 * AWS region code where the KMS key to be used is provisioned. This property
	 * corresponds to the key {@code com.behl.encryptor.aws.region} in the active
	 * {@code .yml} configuration file.
	 * </p>
	 * 
	 * @see https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-regions-availability-zones.html#concepts-available-regions
	 */
	@NotBlank(message = "AWS region must be configured")
	private String region;
	
	/**
	 * <p>
	 * Custom endpoint URL to be used when connecting with AWS KMS service. This
	 * property corresponds to the key <code>com.behl.encryptor.aws.endpoint</code>
	 * in the active .yaml configuration file.
	 * </p>
	 * 
	 * <p>
	 * This property is optional and is only used when running integration tests or
	 * during local development when using LocalStack.
	 * </p>
	 */
	private String endpoint;

	@Valid
	private KMS kms = new KMS();

	@Getter
	@Setter
	public class KMS {

		/**
		 * <p>
		 * The KeyID corresponding to provisioned AWS KMS Key. This property corresponds
		 * to the key {@code com.behl.encryptor.aws.kms.key-id} in the active
		 * {@code .yml} configuration file.
		 * </p>
		 *
		 * <p>
		 * <b> Note: </b>
		 * <ul>
		 *   <li>The configured KMS key should be a Symmetric Key</li>
		 *   <li>The value can be either the KeyID or the Key ARN.</li>
		 * </ul>
		 * </p>
		 */
		@NotBlank(message = "AWS KMS key ID must be configured")
		private String keyId;

	}

}