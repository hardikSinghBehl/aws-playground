package com.behl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.NonNull;

public class InvalidQueryException extends ResponseStatusException {

	private static final long serialVersionUID = -5430679349570223943L;

	public InvalidQueryException(@NonNull final String reason) {
		super(HttpStatus.BAD_REQUEST, reason);
	}

}
