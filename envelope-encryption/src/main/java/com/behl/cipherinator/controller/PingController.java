package com.behl.cipherinator.controller;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/cipherinator")
public class PingController {

	@GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<?> healthCheck() {
		final var response = new HashMap<String, String>();
		response.put("message", "pong");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}