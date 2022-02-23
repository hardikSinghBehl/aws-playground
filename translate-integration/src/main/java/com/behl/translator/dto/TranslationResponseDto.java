package com.behl.translator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * @author Hardik Singh Behl
 * @email behl.hardiksingh@gmail.com
 */

@Getter
@Builder
@Jacksonized
public class TranslationResponseDto {

	private final String translatedText;
	private final String originalText;
	private final String sourceLanguageCode;
	private final String targetLanguageCode;

}
