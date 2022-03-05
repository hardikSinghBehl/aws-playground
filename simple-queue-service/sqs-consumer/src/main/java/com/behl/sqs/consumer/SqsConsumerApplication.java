package com.behl.sqs.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;

import com.behl.sqs.consumer.dto.SuperHeroDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(exclude = { ContextRegionProviderAutoConfiguration.class, ContextStackAutoConfiguration.class })
@Slf4j
public class SqsConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqsConsumerApplication.class, args);
	}

	@SqsListener(value = "secret-superhero-information")
	public void consumer(final String message) throws JsonMappingException, JsonProcessingException {
		final SuperHeroDto superHero = new ObjectMapper().readValue(message, SuperHeroDto.class);

		log.info("RECIEVED SECRET SUPERHERO INFORMATION {}", superHero.toString());
	}

}
