package com.behl.monjo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.behl.monjo.service.StorageService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/files")
public class StorageController {

	private final StorageService storageService;

	@PostMapping
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<HttpStatus> save(@RequestPart(name = "file", required = true) final MultipartFile file) {
		return ResponseEntity.status(storageService.save(file)).build();
	}

	@GetMapping(value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<S3Object> retrive(@PathVariable(required = true, name = "key") final String objectKey) {
		return ResponseEntity.ok(storageService.retrieve(objectKey));
	}

}
