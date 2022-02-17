package com.behl.grundy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Hardik Singh Behl
 */

@Data
@ConfigurationProperties(prefix = "com.behl.aws")
public class AwsProperties {

	private S3 s3 = new S3();

	@Data
	public class S3 {
		/**
		 * S3 Bucket name for which IAM Role has been configured to perform CRUD
		 * operations
		 */
		private String bucketName;
	}

}