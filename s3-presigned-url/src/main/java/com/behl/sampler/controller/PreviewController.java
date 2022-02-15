package com.behl.sampler.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.sampler.service.StorageService;

import lombok.AllArgsConstructor;

/**
 * @author Hardik Singh Behl
 */

@RestController
@AllArgsConstructor
@RequestMapping(value = "/premium/video")
public class PreviewController {

	private final StorageService storageService;

	@GetMapping(value = "/preview/{objectKey}")
	@ResponseStatus(HttpStatus.FOUND)
	public ResponseEntity<?> previewLinkRetreivalHandler(
			@PathVariable(required = true, name = "objectKey") final String objectKey) {
		final URI presignedUrl = storageService.retreivePresignedUrl(objectKey);
		return ResponseEntity.status(HttpStatus.FOUND).location(presignedUrl).build();
	}

}
