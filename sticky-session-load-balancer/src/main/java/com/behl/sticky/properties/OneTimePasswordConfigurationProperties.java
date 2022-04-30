package com.behl.sticky.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.behl.sticky")
public class OneTimePasswordConfigurationProperties {

	private OTP otp = new OTP();

	@Data
	public class OTP {
		/**
		 * Defines minutes after generation until which OTP can be used for validation
		 */
		private Integer expirationMinutes;
	}

}
