package com.behl.cipherinator.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EncryptionResultDto {

	private String encryptedData;
	private String encryptedDataKey;

}
