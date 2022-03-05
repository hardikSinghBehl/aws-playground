package com.behl.polly.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.polly.dto.TextToAudioConversionRequest;
import com.behl.polly.service.AudioGenerationService;

import lombok.AllArgsConstructor;

/**
 * @author Hardik Singh Behl
 * @email behl.hardiksingh@gmail.com
 */

@RestController
@AllArgsConstructor
public class AudioGenerationController {

	private final AudioGenerationService audioGenerationService;

	@PostMapping(value = "/v1/generate/audio")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<InputStreamResource> audioGenerationHandler(
			@RequestBody(required = true) final TextToAudioConversionRequest textToAudioConversionRequest) {
		final var result = new InputStreamResource(
				audioGenerationService.generate(textToAudioConversionRequest.getText()));
		return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment;filename=audio_output_" + System.currentTimeMillis() + ".mp3").body(result);
	}

}
