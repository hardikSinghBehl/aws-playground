package com.behl.sqs.publisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.behl.sqs.publisher.service.SuperHeroService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;

import lombok.AllArgsConstructor;

@SpringBootApplication
@EnableScheduling
@AllArgsConstructor
public class SqsPublisherApplication {

	private final SuperHeroService superHeroService;

	public static void main(String[] args) {
		SpringApplication.run(SqsPublisherApplication.class, args);
	}

	@Scheduled(fixedRate = 4000)
	public void publish() throws JsonProcessingException {
		superHeroService.deliver(new Faker().superhero());
	}

}
