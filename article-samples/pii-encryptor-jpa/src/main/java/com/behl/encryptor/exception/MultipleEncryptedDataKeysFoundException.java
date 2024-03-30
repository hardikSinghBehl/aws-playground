package com.behl.encryptor.exception;

import lombok.NonNull;

public class MultipleEncryptedDataKeysFoundException extends IllegalArgumentException {

	private static final long serialVersionUID = -595222552079359453L;

	public MultipleEncryptedDataKeysFoundException(@NonNull String message) {
		super(message);
	}

}
