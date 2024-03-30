package com.behl.encryptor.exception;

public class MissingEncryptableFieldsException extends IllegalArgumentException {

	private static final long serialVersionUID = -5164309422062063177L;

	private static final String DEFAULT_MESSAGE = "Deposit account must be created prior to performing this operation";

	public MissingEncryptableFieldsException() {
		super(DEFAULT_MESSAGE);
	}

}
