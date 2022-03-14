package com.behl.notification.listener;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.behl.notification.dto.UserCreationRequestDto;
import com.behl.notification.event.UserAccountCreationEvent;
import com.behl.notification.properties.SnsConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@EnableConfigurationProperties(value = SnsConfigurationProperties.class)
public class UserAccountCreationListener {

	private final SnsConfigurationProperties snsConfigurationProperties;
	private final AmazonSNS amazonSNS;

	@EventListener
	@Async
	public void listenToUserAccountCreationEvent(UserAccountCreationEvent userAccountCreationEvent) {
		final var userCreationRequestDto = (UserCreationRequestDto) userAccountCreationEvent.getSource();
		try {
			amazonSNS.publish(snsConfigurationProperties.getTopicArn(),
					new ObjectMapper().writeValueAsString(userCreationRequestDto));
			log.info("User account creation event published to SNS topic {} successfully",
					snsConfigurationProperties.getTopicArn());
		} catch (final JsonProcessingException exception) {
			log.error("Unable to publish event to SNS topic", exception);
		}
	}

}