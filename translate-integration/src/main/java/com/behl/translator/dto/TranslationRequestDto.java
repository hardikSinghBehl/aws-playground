package com.behl.translator.dto;

import com.behl.translator.constant.Language;

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
public class TranslationRequestDto {

	private final String text;
	private final Language sourceLanguage;
	private final Language targetLanguage;

}
