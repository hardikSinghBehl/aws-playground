package com.behl.cipherinator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.NonNull;

public class InvalidMedicalRecordIdException extends ResponseStatusException {

	private static final long serialVersionUID = -4710066739965824560L;
	
	private static final String DEFAULT_REASON = "Invalid medical record Id provided.";

	public InvalidMedicalRecordIdException() {
		super(HttpStatus.NOT_FOUND, DEFAULT_REASON);
	}

	public InvalidMedicalRecordIdException(@NonNull final String reason) {
		super(HttpStatus.NOT_FOUND, reason);
	}

}