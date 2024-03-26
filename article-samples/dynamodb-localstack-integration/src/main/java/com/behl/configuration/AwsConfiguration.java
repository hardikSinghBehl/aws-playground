package com.behl.configuration;

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

import lombok.RequiredArgsConstructor;

/** 
 * <P>
 * Configuration class responsible for configuration of AWS DynamoDB client and 
 * DynamoDB mapper to facilitate interaction within the application.
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
