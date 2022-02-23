package com.behl.moderator.bean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.behl.moderator.properties.AwsIAMConfigurationProperties;

import lombok.AllArgsConstructor;

/**
 * @author Hardik Singh Behl
 * @email behl.hardiksingh@gmail.com
 */

@Configuration
@EnableConfigurationProperties(value = AwsIAMConfigurationProperties.class)
@AllArgsConstructor
public class AwsRekognition {

	private final AwsIAMConfigurationProperties awsIAMConfigurationProperties;

	@Bean
	public AmazonRekognition amazonRekognition() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(awsIAMConfigurationProperties.getAccessKey(),
				awsIAMConfigurationProperties.getSecretAccessKey());

		return AmazonRekognitionClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

}
