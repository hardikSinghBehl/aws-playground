package com.behl.polly.bean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.behl.polly.properties.AwsIAMConfigurationProperties;

import lombok.AllArgsConstructor;

@Configuration
@EnableConfigurationProperties(value = AwsIAMConfigurationProperties.class)
@AllArgsConstructor
public class AmazonPollyConfigurationProperties {

	private final AwsIAMConfigurationProperties awsIAMConfigurationProperties;

	@Bean
	public AmazonPolly amazonPolly() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(awsIAMConfigurationProperties.getAccessKey(),
				awsIAMConfigurationProperties.getSecretAccessKey());

		return AmazonPollyClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

}
