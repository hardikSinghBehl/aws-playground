package com.behl.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.behl.dto.AccountDeactivationRequestDto;
import com.behl.dto.CancelAccountDeactivationRequestDto;
import com.behl.service.UserAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/deactivate")
public class UserDeactivationController {

	private final UserAccountService userAccountService;

	@DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deactivateAccount(
			@Valid @RequestBody AccountDeactivationRequestDto request) {
		userAccountService.deactivateAccount(request);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/cancel", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> cancelAccountDeactivation(
			@Valid @RequestBody CancelAccountDeactivationRequestDto request) {
		userAccountService.cancelAccountDeactivation(request);
		return ResponseEntity.noContent().build();
	}

}