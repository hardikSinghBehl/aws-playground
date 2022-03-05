package com.behl.notification.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.behl.notification.dto.UserCreationRequestDto;
import com.behl.notification.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> userCreationHandler(
			@RequestBody(required = true) final UserCreationRequestDto userCreationRequestDto) {
		userService.createUser(userCreationRequestDto);
		return ResponseEntity.ok().build();
	}

}
