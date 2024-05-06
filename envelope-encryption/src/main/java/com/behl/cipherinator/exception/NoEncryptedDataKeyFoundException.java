package com.behl.cipherinator.exception;

public class NoEncryptedDataKeyFoundException extends IllegalArgumentException {

	private static final long serialVersionUID = 7193838646506459354L;
	
	private static final String DEFAULT_MESSAGE = "No field annotated with @EncryptedDataKey found in the target class.";

	public NoEncryptedDataKeyFoundException() {
		super(DEFAULT_MESSAGE);
	}

}
