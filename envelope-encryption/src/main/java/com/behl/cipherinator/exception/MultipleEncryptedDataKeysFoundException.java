package com.behl.cipherinator.exception;

public class MultipleEncryptedDataKeysFoundException extends IllegalArgumentException {

	private static final long serialVersionUID = -673085618640661576L;
	
	private static final String DEFAULT_MESSAGE = "More than one field annotated with @EncryptedDataKey found in the target class.";

	public MultipleEncryptedDataKeysFoundException() {
		super(DEFAULT_MESSAGE);
	}

}
