package com.behl.cipherinator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.NonNull;

public class InvalidLoginCredentialException extends ResponseStatusException {

	private static final long serialVersionUID = -1704053394998076512L;
	private static final String DEFAULT_REASON = "Invalid login credentials provided.";

	public InvalidLoginCredentialException() {
		super(HttpStatus.UNAUTHORIZED, DEFAULT_REASON);
	}

	public InvalidLoginCredentialException(@NonNull final String reason) {
		super(HttpStatus.UNAUTHORIZED, reason);
	}

}