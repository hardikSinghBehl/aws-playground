package com.behl.chubby.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.behl.chubby.properties.AwsIAMConfigurationProperties;
import com.behl.chubby.properties.AwsS3ConfigurationProperties;
import com.behl.chubby.utility.FileSizeConverter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(value = { AwsIAMConfigurationProperties.class, AwsS3ConfigurationProperties.class })
public class AwsS3Configuration {

	private final AwsIAMConfigurationProperties awsIAMConfigurationProperties;
	private final AwsS3ConfigurationProperties awsS3ConfigurationProperties;

	@Bean
	@Primary
	public AmazonS3 amazonS3() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(awsIAMConfigurationProperties.getAccessKey(),
				awsIAMConfigurationProperties.getSecretAccessKey());
		return AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
	}

	@Bean
	@Primary
	public TransferManager transferManager() {
		final Long multipartObjectSize = FileSizeConverter.getMb()
				.inBytes(awsS3ConfigurationProperties.getS3().getMultipartObjectSize());
		final Long mutlipartThreshold = FileSizeConverter.getMb()
				.inBytes(awsS3ConfigurationProperties.getS3().getMultipartThreshold());
		return TransferManagerBuilder.standard().withS3Client(amazonS3())
				.withMultipartUploadThreshold(mutlipartThreshold).withMinimumUploadPartSize(multipartObjectSize)
				.build();
	}

}