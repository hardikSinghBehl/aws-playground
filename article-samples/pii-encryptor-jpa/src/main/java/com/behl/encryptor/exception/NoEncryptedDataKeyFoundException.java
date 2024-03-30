package com.behl.encryptor.exception;

public class NoEncryptedDataKeyFoundException extends IllegalArgumentException {

	private static final long serialVersionUID = -4927969296705441208L;

	private static final String DEFAULT_MESSAGE = "No field annotated with @EncryptedDataKey found in the target class.";

	public NoEncryptedDataKeyFoundException() {
		super(DEFAULT_MESSAGE);
	}

}
