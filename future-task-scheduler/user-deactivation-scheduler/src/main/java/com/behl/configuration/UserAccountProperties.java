package com.behl.configuration;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "com.behl.user.account")
public class UserAccountProperties {

	@NotNull(message = "User account deactivation delay must be configured")
	private Duration deactivationDelay;

}