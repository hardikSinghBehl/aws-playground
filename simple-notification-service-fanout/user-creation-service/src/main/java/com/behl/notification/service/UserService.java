package com.behl.notification.service;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.behl.notification.dto.UserCreationRequestDto;
import com.behl.notification.event.UserAccountCreationEvent;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final ApplicationEventPublisher applicationEventPublisher;

	public void createUser(final UserCreationRequestDto userCreationRequestDto) {
		// Imaginary code to save user to imaginary database here

		// Publish to spring event
		applicationEventPublisher.publishEvent(new UserAccountCreationEvent(UserCreationRequestDto.builder()
				.emailId(userCreationRequestDto.getEmailId()).userId(UUID.randomUUID()).build()));
	}

}
