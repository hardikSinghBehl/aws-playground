package com.behl.cipherinator.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Configuration class responsible for setting up AWS components based on the
 * properties defined in the active {@code .yml} configuration file and mapped
 * to {@link AwsConfigurationProperties}.
 * </P>
 * 
 * <P>
 * It includes the configuration of AWS Key Management Service (KMS) clients
 * and Amazon DynamoDB clients for different profiles, along with
 * DynamoDBMapper for interaction with DynamoDB.
 * </p>
 * 
 * <p>
 * <b>Profiles:</b>
 * <ul>
 *     <li><strong>Default:</strong> Configures <b>legitimate</b> AWS clients.</li>
 *     <li><strong>Test & Local:</strong> Configures <b>emulated</b> AWS clients with specified custom
 *         endpoints for testing and local development, using the values from
 *         {@code AwsConfigurationProperties.endpoint}.</li>
 * </ul>
 * </p>
 * 
 * @see AwsConfigurationProperties
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AwsConfigurationProperties.class)
public class AwsConfiguration {

	private final AwsConfigurationProperties awsConfigurationProperties;

	@Bean
	@Profile("!test & !local")
	public AWSKMS awsKms() {
		final var credentials = constructCredentials();
		final var regionName = awsConfigurationProperties.getRegion();
		return AWSKMSClientBuilder.standard()
				.withRegion(Regions.fromName(regionName))
				.withCredentials(credentials)
				.build();
	}

	@Bean
	@Profile("!test && !local")
	public AmazonDynamoDB amazonDynamoDB() {
		final var credentials = constructCredentials();
		final var regionName = awsConfigurationProperties.getRegion();
		return AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.fromName(regionName))
				.withCredentials(credentials)
				.build();
	}
	
	@Bean
	@Profile("test | local")
	public AWSKMS testAwsKms() {
		final var credentials = constructCredentials();
		final var regionName = awsConfigurationProperties.getRegion();
		final var endpointUri = awsConfigurationProperties.getEndpoint();
		final var endpointConfiguration = new EndpointConfiguration(endpointUri, regionName);
		return AWSKMSClientBuilder.standard()
				.withEndpointConfiguration(endpointConfiguration)
				.withCredentials(credentials)
				.build();
	}
	
	@Bean
	@Profile("test | local")
	public AmazonDynamoDB testAmazonDynamoDb() {
		final var credentials = constructCredentials();
		final var regionName = awsConfigurationProperties.getRegion();
		final var endpointUri = awsConfigurationProperties.getEndpoint();
		final var endpointConfiguration = new EndpointConfiguration(endpointUri, regionName);
		return AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(endpointConfiguration)
				.withCredentials(credentials)
				.build();
	}

	@Bean
	public DynamoDBMapper dynamoDBMapper(final AmazonDynamoDB amazonDynamoDB) {
		return new DynamoDBMapper(amazonDynamoDB);
	}

	private AWSStaticCredentialsProvider constructCredentials() {
		final var accessKey = awsConfigurationProperties.getAccessKey();
		final var secretAccessKey = awsConfigurationProperties.getSecretAccessKey();
		final var basicAwsCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);
		return new AWSStaticCredentialsProvider(basicAwsCredentials);
	}
	
}
