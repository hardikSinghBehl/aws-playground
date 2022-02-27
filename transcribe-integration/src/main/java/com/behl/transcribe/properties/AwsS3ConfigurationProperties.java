package com.behl.transcribe.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Hardik Singh Behl
 */

@Data
@ConfigurationProperties(prefix = "com.behl.aws")
public class AwsS3ConfigurationProperties {

	private S3 s3 = new S3();

	@Data
	public class S3 {
		/**
		 * S3 Bucket name for which where input media files are to be saved
		 */
		private String inputBucketName;

		/**
		 * S3 Bucket name for which where output media files are to be saved
		 */
		private String outputBucketName;

	}

}