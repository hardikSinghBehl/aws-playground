package com.behl.event.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.behl.event.log.dto.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(exclude = { ContextRegionProviderAutoConfiguration.class, ContextStackAutoConfiguration.class })
@Slf4j
public class EventLogSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventLogSystemApplication.class, args);
	}

	@SqsListener(value = "event-log-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void consumer(@NotificationMessage final String message) {
		User user;
		try {
			user = new ObjectMapper().readValue(message, User.class);
		} catch (JsonProcessingException exception) {
			log.error("Unable to read notification message", exception);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("ACCOUNT CREATED FOR {} AND ASSIGNED SYSTEM USER ID {}", user.getEmailId(), user.getUserId());
	}

}
