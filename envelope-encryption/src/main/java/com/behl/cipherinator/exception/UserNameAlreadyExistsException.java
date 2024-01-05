package com.behl.cipherinator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.NonNull;

public class UserNameAlreadyExistsException extends ResponseStatusException {

	private static final long serialVersionUID = 8815463728027186891L;

	public UserNameAlreadyExistsException(@NonNull final String reason) {
		super(HttpStatus.CONFLICT, reason);
	}

}
