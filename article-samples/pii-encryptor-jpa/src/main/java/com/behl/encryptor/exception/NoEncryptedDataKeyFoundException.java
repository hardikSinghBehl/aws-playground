package com.behl.encryptor.exception;

import lombok.NonNull;

public class NoEncryptedDataKeyFoundException extends IllegalArgumentException {

	private static final long serialVersionUID = -4927969296705441208L;

	public NoEncryptedDataKeyFoundException(@NonNull String message) {
		super(message);
	}

}
