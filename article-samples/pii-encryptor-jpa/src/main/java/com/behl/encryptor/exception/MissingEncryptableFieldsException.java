package com.behl.encryptor.exception;

import lombok.NonNull;

public class MissingEncryptableFieldsException extends IllegalArgumentException {

	private static final long serialVersionUID = -5164309422062063177L;

	public MissingEncryptableFieldsException(@NonNull String message) {
		super(message);
	}

}
