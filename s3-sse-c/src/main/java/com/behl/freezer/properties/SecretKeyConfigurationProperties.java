package com.behl.freezer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Hardik Singh Behl
 * @email behl.hardiksingh@gmail.com
 */

@Data
@ConfigurationProperties(prefix = "com.behl")
public class SecretKeyConfigurationProperties {

	private SecretKey secretKey = new SecretKey();

	@Data
	public class SecretKey {
		/**
		 * A random value to convert the secret key value to an encrypted secret-key
		 */
		private String salt;

		/**
		 * Value which will be used to generate the customer managed key for formforming
		 * S3 operations
		 */
		private String value;
	}

}
