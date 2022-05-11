package com.behl.cipherinator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.cipherinator.dto.UserLoginRequestDto;
import com.behl.cipherinator.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/cipherinator")
public class AuthenticationController {

	private final UserService userService;

	@PostMapping(value = "/users/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<HttpStatus> userLoginRequestHandler(
			@RequestBody(required = true) final UserLoginRequestDto userLoginRequestDto) {
		return ResponseEntity.status(userService.login(userLoginRequestDto)).build();
	}

}
