package com.behl.monjo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.behl.aws")
public class AwsKmsConfigurationProperties {

	private KMS kms = new KMS();

	@Data
	public class KMS {
		private String keyArn;
	}

}