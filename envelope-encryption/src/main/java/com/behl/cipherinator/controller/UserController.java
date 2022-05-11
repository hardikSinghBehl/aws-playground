package com.behl.cipherinator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.cipherinator.dto.UserCreationRequestDto;
import com.behl.cipherinator.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/cipherinator")
public class UserController {

	private final UserService userService;

	@PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<HttpStatus> userCreationRequestHandler(
			@RequestBody(required = true) final UserCreationRequestDto userCreationRequestDto) {
		return ResponseEntity.status(userService.create(userCreationRequestDto)).build();
	}

}
