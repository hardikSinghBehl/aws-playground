package com.behl.monjo.bean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.behl.monjo.properties.AwsIAMConfigurationProperties;
import com.behl.monjo.properties.AwsKmsConfigurationProperties;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(value = { AwsKmsConfigurationProperties.class, AwsIAMConfigurationProperties.class })
public class KmsMasterKeyProviderBean {

	private final AwsKmsConfigurationProperties awsKmsConfigurationProperties;
	private final AwsIAMConfigurationProperties awsIAMConfigurationProperties;

	@Bean
	@Primary
	public KmsMasterKeyProvider kmsMasterKeyProvider() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(awsIAMConfigurationProperties.getAccessKey(),
				awsIAMConfigurationProperties.getSecretAccessKey());
		return KmsMasterKeyProvider.builder().withDefaultRegion("ap-south-1")
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.buildStrict(awsKmsConfigurationProperties.getKms().getKeyArn());
	}

}
