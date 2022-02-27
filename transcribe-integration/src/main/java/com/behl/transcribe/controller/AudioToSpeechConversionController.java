package com.behl.transcribe.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.behl.transcribe.service.AudioToSpeechConvertionService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AudioToSpeechConversionController {

	private final AudioToSpeechConvertionService audioToSpeechConvertionService;

	@PostMapping(value = "/audio/conversion/language/{languageCode}")
	public ResponseEntity<InputStreamResource> ds(@RequestPart(name = "file", required = true) final MultipartFile file,
			@PathVariable(name = "languageCode") final String languageCode) {
		final var result = new InputStreamResource(audioToSpeechConvertionService.convert(file, languageCode));
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=result.json").body(result);
	}

}
