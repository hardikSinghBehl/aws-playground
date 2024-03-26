package com.behl.encryptor.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;

import lombok.RequiredArgsConstructor;

/**
 * <P>
 * Configuration class responsible for configuration of AWS Key Management
 * Service (KMS) client to facilitate interaction within the application
 * </p>
 * 
 * @see com.behl.encryptor.configuration.AwsConfigurationProperties
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AwsConfigurationProperties.class)
public class AwsConfiguration {

	private final AwsConfigurationProperties awsConfigurationProperties;

	@Bean
	public AWSKMS awsKms() {
		var credentials = constructCredentials();
		var regionName = awsConfigurationProperties.getRegion();
		return AWSKMSClientBuilder.standard()
				.withRegion(Regions.fromName(regionName))
				.withCredentials(credentials)
				.build();
	}

	private AWSStaticCredentialsProvider constructCredentials() {
		var accessKey = awsConfigurationProperties.getAccessKey();
		var secretAccessKey = awsConfigurationProperties.getSecretAccessKey();
		var basicAwsCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);
		return new AWSStaticCredentialsProvider(basicAwsCredentials);
	}
	
}
