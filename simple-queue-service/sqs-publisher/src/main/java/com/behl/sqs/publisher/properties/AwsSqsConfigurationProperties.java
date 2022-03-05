package com.behl.sqs.publisher.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Hardik Singh Behl
 * @email behl.hardiksingh@gmail.com
 */

@Data
@ConfigurationProperties(prefix = "com.behl.aws.sqs")
public class AwsSqsConfigurationProperties {

	private String queueUrl;

}
