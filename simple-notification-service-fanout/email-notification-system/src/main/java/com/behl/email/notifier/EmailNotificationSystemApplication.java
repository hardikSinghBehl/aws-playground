package com.behl.email.notifier;

import java.time.LocalDateTime;

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
import com.behl.email.notifier.dto.User;
import com.behl.email.notifier.properties.AwsSqsConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(exclude = { ContextRegionProviderAutoConfiguration.class, ContextStackAutoConfiguration.class })
@Slf4j
@AllArgsConstructor
@EnableConfigurationProperties(AwsSqsConfigurationProperties.class)
public class EmailNotificationSystemApplication {

	private final AmazonSQSAsync amazonSQSAsync;
	private final AwsSqsConfigurationProperties awsSqsConfigurationProperties;

	public static void main(String[] args) {
		SpringApplication.run(EmailNotificationSystemApplication.class, args);
	}

	@SqsListener(value = "email-notification-queue")
	public void consumer(@NotificationMessage final String message,
			@Header(name = "ReceiptHandle") final String receiptHandle) {
		User user;
		try {
			user = new ObjectMapper().readValue(message, User.class);
		} catch (JsonProcessingException exception) {
			log.error("Unable to read notification message", exception);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		log.info("SENDING ACCOUNT CONFIRMATION EMAIL TO USER {}: {}", user.getEmailId(), LocalDateTime.now());
		amazonSQSAsync.deleteMessage(awsSqsConfigurationProperties.getUrl(), receiptHandle);
	}

}
