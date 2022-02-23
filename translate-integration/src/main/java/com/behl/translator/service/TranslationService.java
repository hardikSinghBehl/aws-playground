package com.behl.translator.service;

import org.springframework.stereotype.Service;

import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import com.behl.translator.dto.TranslationRequestDto;
import com.behl.translator.dto.TranslationResponseDto;

import lombok.AllArgsConstructor;

/**
 * @author Hardik Singh Behl
 * @email behl.hardiksingh@gmail.com
 */

@Service
@AllArgsConstructor
public class TranslationService {

	private final AmazonTranslate amazonTranslate;

	/**
	 * Translates the provided text in the source language to the provided target
	 * language
	 * 
	 * @param TranslationRequestDto.class
	 */
	public TranslationResponseDto translate(final TranslationRequestDto translationRequestDto) {
		final TranslateTextRequest request = new TranslateTextRequest().withText(translationRequestDto.getText())
				.withSourceLanguageCode(translationRequestDto.getSourceLanguage().getLanguageCode())
				.withTargetLanguageCode(translationRequestDto.getTargetLanguage().getLanguageCode());

		final TranslateTextResult result = amazonTranslate.translateText(request);

		return TranslationResponseDto.builder().translatedText(result.getTranslatedText())
				.originalText(translationRequestDto.getText()).sourceLanguageCode(result.getSourceLanguageCode())
				.targetLanguageCode(result.getTargetLanguageCode()).build();
	}

}
