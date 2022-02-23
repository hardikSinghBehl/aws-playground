package com.behl.transcribe.bean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.behl.transcribe.properties.AwsIAMConfigurationProperties;

import lombok.AllArgsConstructor;

@Configuration
@EnableConfigurationProperties(value = AwsIAMConfigurationProperties.class)
@AllArgsConstructor
public class AmazonTranscribeConfiguration {

	private final AwsIAMConfigurationProperties awsIAMConfigurationProperties;

	@Bean
	public AmazonTranscribe amazonTranscribe() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(awsIAMConfigurationProperties.getAccessKey(),
				awsIAMConfigurationProperties.getSecretAccessKey());

		return AmazonTranscribeClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

}
