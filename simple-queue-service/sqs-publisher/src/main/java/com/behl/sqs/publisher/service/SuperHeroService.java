package com.behl.sqs.publisher.service;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.behl.sqs.publisher.dto.SuperHeroDto;
import com.behl.sqs.publisher.properties.AwsSqsConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Superhero;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@EnableConfigurationProperties(AwsSqsConfigurationProperties.class)
public class SuperHeroService {

	private final AmazonSQS amazonSQS;
	private final AwsSqsConfigurationProperties awsSqsConfigurationProperties;

	public void deliver(final Superhero superHeroInformation) throws JsonProcessingException {
		final var superheroDto = constructSuperHeroDto(superHeroInformation);

		final var sendMessageRequest = new SendMessageRequest()
				.withMessageBody(new ObjectMapper().writeValueAsString(superheroDto))
				.withQueueUrl(awsSqsConfigurationProperties.getQueueUrl());

		amazonSQS.sendMessage(sendMessageRequest);
		log.info("SENT INFORMATION FOR {}", superheroDto);
	}

	private SuperHeroDto constructSuperHeroDto(final Superhero superHeroInformation) {
		return SuperHeroDto.builder().descriptor(superHeroInformation.descriptor()).name(superHeroInformation.name())
				.power(superHeroInformation.power()).prefix(superHeroInformation.prefix())
				.suffix(superHeroInformation.suffix()).build();
	}
}
