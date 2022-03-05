package com.behl.notification.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Hardik Singh Behl
 * @email behl.hardiksingh@gmail.com
 */

@Data
@ConfigurationProperties(prefix = "com.behl.aws.sns")
public class SnsConfigurationProperties {

	private String topicArn;

}
