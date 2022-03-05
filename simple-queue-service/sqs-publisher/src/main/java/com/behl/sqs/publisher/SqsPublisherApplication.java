package com.behl.sqs.publisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.behl.sqs.publisher.dto.SuperHeroDto;
import com.behl.sqs.publisher.properties.AwsSqsConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@AllArgsConstructor
@EnableScheduling
@Slf4j
@EnableConfigurationProperties(AwsSqsConfigurationProperties.class)
public class SqsPublisherApplication {

	private final AmazonSQS amazonSQS;
	private final AwsSqsConfigurationProperties awsSqsConfigurationProperties;

	public static void main(String[] args) {
		SpringApplication.run(SqsPublisherApplication.class, args);
	}

	@Scheduled(fixedRate = 4000)
	public void publish() throws JsonProcessingException {
		final var superHeroInformation = new Faker().superhero();
		final var superheroDto = SuperHeroDto.builder().descriptor(superHeroInformation.descriptor())
				.name(superHeroInformation.name()).power(superHeroInformation.power())
				.prefix(superHeroInformation.prefix()).suffix(superHeroInformation.suffix()).build();

		final var sendMessageRequest = new SendMessageRequest()
				.withMessageBody(new ObjectMapper().writeValueAsString(superheroDto))
				.withQueueUrl(awsSqsConfigurationProperties.getQueueUrl());

		amazonSQS.sendMessage(sendMessageRequest);
		log.info("SENT INFORMATION FOR {}", superheroDto);
	}

}
