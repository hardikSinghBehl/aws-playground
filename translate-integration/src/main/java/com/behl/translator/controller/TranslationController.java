package com.behl.translator.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.behl.translator.constant.Language;
import com.behl.translator.dto.TranslationRequestDto;
import com.behl.translator.dto.TranslationResponseDto;
import com.behl.translator.service.TranslationService;

import lombok.AllArgsConstructor;

/**
 * @author Hardik Singh Behl
 * @email behl.hardiksingh@gmail.com
 */

@RestController
@AllArgsConstructor
@RequestMapping(value = "/translation")
public class TranslationController {

	private final TranslationService translationService;

	/**
	 * Endpoint to return all supported source and target languages in AWS that can
	 * be used for translation
	 * 
	 * @return List of all supported languages
	 */
	@GetMapping(value = "/language", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Language>> retreiveSupportedLanguages() {
		return ResponseEntity.ok(Arrays.asList(Language.values()));
	}

	/**
	 * endpoint to translate text in the source language to target language
	 * 
	 * @param TranslationRequestDto.class
	 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TranslationResponseDto> translationHandler(
			@RequestBody(required = true) final TranslationRequestDto translationRequestDto) {
		return ResponseEntity.ok(translationService.translate(translationRequestDto));
	}

}
