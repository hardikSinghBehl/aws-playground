package com.behl.grundy.bean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.behl.grundy.properties.AwsProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Hardik Singh Behl
 */

@Configuration
@EnableConfigurationProperties(value = AwsProperties.class)
@Slf4j
public class AwsCredentialProvider {

	private final AwsProperties awsProperties;
	private final AWSCredentialsProvider awsCredentialsProvider;

	@Lazy
	public AwsCredentialProvider(final AwsProperties awsProperties,
			final AWSCredentialsProvider awsCredentialsProvider) {
		this.awsProperties = awsProperties;
		this.awsCredentialsProvider = awsCredentialsProvider;
	}

	@Bean
	@Primary
	public AWSCredentialsProvider awsCredentialsProvider() {
		log.info("Assuming role: {}", awsProperties.getRoleArn());
		AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
				.withCredentials(awsCredentialsProvider).build();

		return new STSAssumeRoleSessionCredentialsProvider.Builder(awsProperties.getRoleArn(), "role")
				.withStsClient(stsClient).build();
	}

}
