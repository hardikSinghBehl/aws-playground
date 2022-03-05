package com.behl.event.log.bean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.behl.event.log.properties.AwsIAMConfigurationProperties;

import lombok.AllArgsConstructor;

@Configuration
@EnableConfigurationProperties(value = AwsIAMConfigurationProperties.class)
@AllArgsConstructor
public class SqsBeanConfiguration {

	private final AwsIAMConfigurationProperties awsIAMConfigurationProperties;

	@Bean
	@Primary
	public AmazonSQSAsync amazonSQSAsync() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(awsIAMConfigurationProperties.getAccessKey(),
				awsIAMConfigurationProperties.getSecretAccessKey());

		return AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

}
