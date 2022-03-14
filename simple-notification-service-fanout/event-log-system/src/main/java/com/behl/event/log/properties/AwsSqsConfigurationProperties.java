package com.behl.event.log.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.behl.aws.sqs")
public class AwsSqsConfigurationProperties {

	private String url;

}