package com.behl.cipherinator.exception;

public class MissingEncryptableFieldsException extends IllegalArgumentException {

	private static final long serialVersionUID = -7407374393487691899L;
	
	private static final String DEFAULT_MESSAGE = "No valid field annotated with @Encryptable found in the target class.";

	public MissingEncryptableFieldsException() {
		super(DEFAULT_MESSAGE);
	}

}
