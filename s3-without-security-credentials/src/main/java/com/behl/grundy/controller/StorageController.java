package com.behl.grundy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.behl.grundy.service.StorageService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/files")
public class StorageController {

	private final StorageService storageService;

	@PostMapping
	@ResponseStatus(value = HttpStatus.OK)
	public HttpStatus save(@RequestPart(name = "file", required = true) final MultipartFile file) {
		return storageService.save(file);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<List<String>> listFiles() {
		return ResponseEntity.ok(storageService.listFiles());
	}

}
