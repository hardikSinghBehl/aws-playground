package com.behl.encryptor.exception;

import lombok.NonNull;

public class InvalidEncryptedDataKeyTypeException extends IllegalArgumentException {

	private static final long serialVersionUID = -3819871290359089216L;

	public InvalidEncryptedDataKeyTypeException(@NonNull String message) {
		super(message);
	}

}
