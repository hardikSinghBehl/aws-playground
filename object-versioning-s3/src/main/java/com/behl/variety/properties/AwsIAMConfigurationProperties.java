package com.behl.variety.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.behl.aws")
public class AwsIAMConfigurationProperties {

	private String accessKey;
	private String secretAccessKey;

}