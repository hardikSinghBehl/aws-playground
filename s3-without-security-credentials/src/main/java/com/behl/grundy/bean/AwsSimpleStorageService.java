package com.behl.grundy.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * @author Hardik Singh Behl
 */

@Configuration
public class AwsSimpleStorageService {

	@Bean
	@Primary
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new InstanceProfileCredentialsProvider(false)).build();
	}

}
