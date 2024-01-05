package com.behl.cipherinator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.behl.cipherinator.dto.ExceptionResponseDto;
import com.behl.cipherinator.dto.UserLoginRequestDto;
import com.behl.cipherinator.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthenticationController {

	private final UserService userService;

	@PostMapping(value = "/api/v1/users/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Logs in user into the system", description = "Returns 200 HTTP Response corresponding to valid login credentials")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Authentication successfull",
					content = @Content(schema = @Schema(implementation = Void.class))),
			@ApiResponse(responseCode = "401", description = "Bad credentials provided. Failed to authenticate user",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Malformed request.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))})
	public ResponseEntity<HttpStatus> login(@Valid @RequestBody final UserLoginRequestDto userLoginRequest) {
		userService.login(userLoginRequest);
		return ResponseEntity.ok().build();
	}

}
