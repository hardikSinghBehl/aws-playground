package com.behl.sampler.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.behl.aws")
public class AmazonS3ConfigurationProperties {

	private Configuration s3 = new Configuration();

	@Data
	public class Configuration {
		private String accessKey;
		private String secretKey;
		private String bucketName;
	}
}