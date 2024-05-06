package com.behl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.NonNull;

public class EmptyResultException extends ResponseStatusException {

	private static final long serialVersionUID = -7272878292705722288L;

	public EmptyResultException(@NonNull final String reason) {
		super(HttpStatus.NOT_FOUND, reason);
	}

}