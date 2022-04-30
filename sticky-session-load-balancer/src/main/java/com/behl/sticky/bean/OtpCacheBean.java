package com.behl.sticky.bean;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.behl.sticky.properties.OneTimePasswordConfigurationProperties;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(OneTimePasswordConfigurationProperties.class)
public class OtpCacheBean {

	private final OneTimePasswordConfigurationProperties oneTimePasswordConfigurationProperties;

	@Bean
	public LoadingCache<String, Integer> loadingCache() {
		final var expirationMinutes = oneTimePasswordConfigurationProperties.getOtp().getExpirationMinutes();
		return CacheBuilder.newBuilder().expireAfterWrite(expirationMinutes, TimeUnit.MINUTES)
				.build(new CacheLoader<>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

}