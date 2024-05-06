package com.behl.cipherinator.exception;

public class InvalidEncryptedDataKeyTypeException extends IllegalArgumentException {

	private static final long serialVersionUID = -1385160713610569870L;
	
	private static final String DEFAULT_MESSAGE = "Field annotated with @EncryptedDataKey must be of type String";

	public InvalidEncryptedDataKeyTypeException() {
		super(DEFAULT_MESSAGE);
	}

}
