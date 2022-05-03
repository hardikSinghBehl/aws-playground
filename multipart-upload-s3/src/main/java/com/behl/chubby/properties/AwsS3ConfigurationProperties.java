package com.behl.chubby.properties;

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
		 * Size in Megabytes of each part in an multipart upload. If any file with total
		 * size less than the configured value is provided to the API, it won't be
		 * uploaded in parts
		 */
		private Integer multipartObjectSize;
	}

}