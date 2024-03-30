package com.behl.encryptor.exception;

public class InvalidEncryptedDataKeyTypeException extends IllegalArgumentException {

	private static final long serialVersionUID = -3819871290359089216L;

	private static final String DEFAULT_MESSAGE = "Field annotated with @EncryptedDataKey must be of type String";

	public InvalidEncryptedDataKeyTypeException() {
		super(DEFAULT_MESSAGE);
	}

}
