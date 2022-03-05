package com.behl.notification.bean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.behl.notification.properties.AwsIAMConfigurationProperties;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(value = AwsIAMConfigurationProperties.class)
public class SimpleNotificationServiceBeanConfiguration {

	private final AwsIAMConfigurationProperties awsIAMConfigurationProperties;

	@Bean
	public AmazonSNS amazonSNS() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(awsIAMConfigurationProperties.getAccessKey(),
				awsIAMConfigurationProperties.getSecretAccessKey());

		return AmazonSNSClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}
}
