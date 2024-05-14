package com.behl.cipherinator.configuration;

import java.net.URI;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.kms.KmsClient;

/**
 * <P>
 * Configuration class responsible for configuration of AWS DynamoDB
 * and KMS clients to facilitate interaction within the application.
 * </p>
 * 
 * <p>
 * <b>Profiles:</b>
 * <ul>
 *     <li><strong>Default:</strong> Configures <b>legitimate</b> AWS clients.</li>
 *     <li><strong>Test & Local:</strong> Configures <b>emulated</b> AWS clients.</li>
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
	public KmsClient kmsClient() {
		final var credentials = constructCredentials();
		final var regionName = awsConfigurationProperties.getRegion();
		return KmsClient.builder()
				.region(Region.of(regionName))
				.credentialsProvider(credentials)
				.build();
	}

	@Bean
	@Profile("!test && !local")
	public DynamoDbClient dynamoDbClient() {
		final var credentials = constructCredentials();
		final var regionName = awsConfigurationProperties.getRegion();
		return DynamoDbClient.builder()
				.region(Region.of(regionName))
				.credentialsProvider(credentials)
				.build();
	}
	
	@Bean
	@Profile("test | local")
	public KmsClient testKmsClient() {
		final var credentials = constructCredentials();
		final var regionName = awsConfigurationProperties.getRegion();
		final var endpointUri = awsConfigurationProperties.getEndpoint();
		return KmsClient.builder()
				.region(Region.of(regionName))
				.endpointOverride(URI.create(endpointUri))
				.credentialsProvider(credentials)
				.build();
	}
	
	@Bean
	@Profile("test | local")
	public DynamoDbClient testDynamoDbClient() {
		final var credentials = constructCredentials();
		final var regionName = awsConfigurationProperties.getRegion();
		final var endpointUri = URI.create(awsConfigurationProperties.getEndpoint());
		return DynamoDbClient.builder()
				.region(Region.of(regionName))
				.endpointOverride(endpointUri)
				.credentialsProvider(credentials)
				.build();
	}
	
	@Bean
	public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
		return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
	}

	private StaticCredentialsProvider constructCredentials() {
		final var accessKey = awsConfigurationProperties.getAccessKey();
		final var secretAccessKey = awsConfigurationProperties.getSecretAccessKey();
		final var awsCredentials = AwsBasicCredentials.create(accessKey, secretAccessKey);
		return StaticCredentialsProvider.create(awsCredentials);
	}
	
}
