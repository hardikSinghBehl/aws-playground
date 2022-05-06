package com.behl.offloader.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.offloader.service.StorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/temporary/upload")
public class StorageController {

	private final StorageService storageService;

	@GetMapping(value = "/{objectKey}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<?> temporaryPutPresignedUriGenerationHandler(
			@PathVariable(name = "objectKey", required = true) final String objectKey) {
		final var presignedUri = storageService.generateTemporaryPutUri(objectKey);
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("URI", presignedUri));
	}

}
