package com.behl.offloader.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.behl.aws")
public class AwsS3ConfigurationProperties {

	private S3 s3 = new S3();

	@Data
	public class S3 {
		private String bucketName;

		/**
		 * Signifies the time in minues after creation until which the generated
		 * presigned URI will be valid
		 */
		private Integer presignedUriExpiration;
	}

}