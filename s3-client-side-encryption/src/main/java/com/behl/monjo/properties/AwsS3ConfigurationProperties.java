package com.behl.monjo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.behl.aws")
public class AwsS3ConfigurationProperties {

	private Configuration s3 = new Configuration();

	@Data
	public class Configuration {
		private String bucketName;
	}
}