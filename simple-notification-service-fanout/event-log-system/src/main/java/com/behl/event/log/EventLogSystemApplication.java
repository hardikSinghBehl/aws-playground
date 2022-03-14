package com.behl.event.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.behl.event.log.dto.User;
import com.behl.event.log.properties.AwsSqsConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(exclude = { ContextRegionProviderAutoConfiguration.class, ContextStackAutoConfiguration.class })
@Slf4j
@AllArgsConstructor
@EnableConfigurationProperties(AwsSqsConfigurationProperties.class)
public class EventLogSystemApplication {

	private final AmazonSQSAsync amazonSQSAsync;
	private final AwsSqsConfigurationProperties awsSqsConfigurationProperties;

	public static void main(String[] args) {
		SpringApplication.run(EventLogSystemApplication.class, args);
	}

	@SqsListener(value = "event-log-queue")
	public void consumer(@NotificationMessage final String message,
			@Header(name = "ReceiptHandle") final String receiptHandle) {
		User user;
		try {
			user = new ObjectMapper().readValue(message, User.class);
		} catch (JsonProcessingException exception) {
			log.error("Unable to read notification message", exception);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		log.info("ACCOUNT CREATED FOR {} AND ASSIGNED SYSTEM USER ID {}", user.getEmailId(), user.getUserId());
		amazonSQSAsync.deleteMessage(awsSqsConfigurationProperties.getUrl(), receiptHandle);
	}

}
