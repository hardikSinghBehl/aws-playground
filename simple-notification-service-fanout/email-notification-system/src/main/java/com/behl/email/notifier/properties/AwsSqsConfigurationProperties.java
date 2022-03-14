package com.behl.email.notifier.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.behl.aws.sqs")
public class AwsSqsConfigurationProperties {

	private String url;

}