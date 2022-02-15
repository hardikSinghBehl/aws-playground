package com.behl.sampler.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping(value = "/preview")
	@ResponseStatus(HttpStatus.FOUND)
	public ResponseEntity<?> previewLinkRetreivalHandler() {
		final URI presignedUrl = storageService.retreivePresignedUrl();
		return ResponseEntity.status(HttpStatus.FOUND).location(presignedUrl).build();
	}

}
