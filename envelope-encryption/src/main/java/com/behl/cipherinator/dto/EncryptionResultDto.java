package com.behl.cipherinator.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class EncryptionResultDto implements Serializable {

	private static final long serialVersionUID = 1845168741797415329L;

	private final String encryptedDataKey;
	private final String encryptedResult;

}
