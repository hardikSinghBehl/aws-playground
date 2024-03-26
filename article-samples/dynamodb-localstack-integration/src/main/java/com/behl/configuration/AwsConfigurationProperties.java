package com.behl.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Maps configuration values defined in the active {@code .yml} file to the
 * corresponding variables below. The configuration properties are referenced
 * within the application to facilitate connection with AWS DynamoDB service.
 * </p>
 *
 * <p>
 * <b> Example YAML configuration: </b>
 * </p>
 * 
 * <pre>
 * com:
 *   behl:
 *     aws:
 *       access-key: "aws-access-key"
 *       secret-access-key: "aws-secret-access-key"
 *       region: "aws-region"
 *       endpoint: "aws-endpoint-url"
 * </pre>
 * 
 * @see com.behl.configuration.AwsConfiguration
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "com.behl.aws")
public class AwsConfigurationProperties {

	/**
	 * <p>
	 * AWS access key for authenticating requests made to AWS DynamoDB.
	 * This property corresponds to the key {@code com.behl.aws.access-key}
	 * in the active {@code .yml} configuration file.
	 * </p>
	 */
	@NotBlank(message = "AWS access key must be configured")
	private String accessKey;

	/**
	 * <p>
	 * AWS secret access key for authenticating requests made to AWS DynamoDB.
	 * This property corresponds to the key {@code com.behl.aws.secret-access-key}
	 * in the active {@code .yml} configuration file.
	 * </p>
	 */
	@NotBlank(message = "AWS secret access key must be configured")
	private String secretAccessKey;

	/**
	 * <p>
	 * AWS region code where the DynamoDB table to be used is provisioned. This property
	 * corresponds to the key {@code com.behl.aws.region} in the active
	 * {@code .yml} configuration file.
	 * </p>
	 * 
	 * @see https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-regions-availability-zones.html#concepts-available-regions
	 */
	@NotBlank(message = "AWS region must be configured")
	private String region;
	
	/**
	 * <p>
	 * Custom endpoint URL to be used when connecting with AWS DynamoDB. This
	 * property corresponds to the key <code>com.behl.aws.endpoint</code> in the
	 * active .yaml configuration file.
	 * </p>
	 * 
	 * <p>
	 * This property is optional and is only used when running integration tests or
	 * during local development when using LocalStack.
	 * </p>
	 */
	private String endpoint;

}