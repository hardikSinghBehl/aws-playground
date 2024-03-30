package com.behl.configuration;

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

/** 
 * <P>
 * Configuration class responsible for configuration of AWS DynamoDB client
 * to facilitate interaction within the application.
 * </p>
 * 
 * <p>
 * <b>Profiles:</b>
 * <ul>
 * <li><strong>Default:</strong> Configures <b>legitimate</b> AWS DynamoDB client.</li>
 * <li><strong>Test & Local:</strong> Configures <b>emulated</b> AWS DynamoDB client
 * with specified custom endpoint in {@code AwsConfigurationProperties.endpoint}.</li>
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
