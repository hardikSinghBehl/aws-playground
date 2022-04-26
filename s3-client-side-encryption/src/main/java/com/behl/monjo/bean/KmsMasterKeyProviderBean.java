package com.behl.monjo.bean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.behl.monjo.properties.AwsKmsConfigurationProperties;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(value = AwsKmsConfigurationProperties.class)
public class KmsMasterKeyProviderBean {

	private final AwsKmsConfigurationProperties awsKmsConfigurationProperties;

	@Bean
	@Primary
	public KmsMasterKeyProvider kmsMasterKeyProvider() {
		return KmsMasterKeyProvider.builder().buildStrict(awsKmsConfigurationProperties.getKms().getKeyArn());
	}

}
