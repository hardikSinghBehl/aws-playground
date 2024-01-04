package com.behl.cipherinator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Maps AWS configuration values defined in the active {@code .yml} file to the
 * corresponding variables below. The configuration properties are referenced
 * within the application to facilitate connection with AWS services.
 * </p>
 *
 * <p>
 * <b> Example YAML configuration: </b>
 * </p>
 * 
 * <pre>
 * com:
 *   behl:
 *     cipherinator:
 *       aws:
 *         access-key: "aws-access-key"
 *         secret-access-key: "aws-secret-access-key"
 *         region: "aws-region"
 *         endpoint: "aws-endpoint-url"
 *         kms:
 *           key-id: "aws-kms-key-id"
 * </pre>
 * 
 * <p>
 * <b> Note: </b>
 * </p>
 * 
 * <ul>
 * <li>The `endpoint` field is optional and is configured only for local
 * development and testing when using LocalStack</li>
 * <li>In the `key-id` field, the KMS Key ARN can also be configured.</li>
 * </ul>
 * 
 * @see com.behl.cipherinator.configuration.AwsConfiguration
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "com.behl.cipherinator.aws")
public class AwsConfigurationProperties {

	/**
	 * <p>
	 * AWS access key for authenticating requests made to AWS services. This
	 * property corresponds to the key {@code com.behl.cipherinator.aws.access-key}
	 * in the active {@code .yml} configuration file.
	 * </p>
	 */
	@NotBlank(message = "AWS access key must be configured")
	private String accessKey;

	/**
	 * <p>
	 * AWS secret access key for authenticating requests made to AWS services. This
	 * property corresponds to the key
	 * {@code com.behl.cipherinator.aws.secret-access-key} in the active
	 * {@code .yml} configuration file.
	 * </p>
	 */
	@NotBlank(message = "AWS secret access key must be configured")
	private String secretAccessKey;

	/**
	 * <p>
	 * AWS region name where the services in context are provisioned. This property
	 * corresponds to the key {@code com.behl.cipherinator.aws.region} in the active
	 * {@code .yml} configuration file.
	 * </p>
	 * 
	 * <p>
	 * <b> Note: </b> Ensure the AWS KMS Key and DynamoDB are provisioned in the
	 * specified region to facilitate communication.
	 * </p>
	 */
	@NotBlank(message = "AWS region must be configured")
	private String region;

	/**
	 * <p>
	 * Custom endpoint URL to be used when connecting with AWS services. This
	 * property corresponds to the key
	 * <code>com.behl.cipherinator.aws.endpoint</code> in the active .yaml
	 * configuration file.
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
		 * to the key {@code com.behl.cipherinator.aws.kms.key-id} in the active
		 * {@code .yml} configuration file.
		 * </p>
		 *
		 * <p>
		 * <b> Note: </b> The value can be either the KeyID or the Key ARN.
		 * Additionally, the configured KMS key should be a symmetric key.
		 * </p>
		 */
		@NotBlank(message = "AWS KMS key ID must be configured")
		private String keyId;

	}

}