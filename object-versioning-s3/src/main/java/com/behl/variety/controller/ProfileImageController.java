package com.behl.variety.controller;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3VersionSummary;
import com.behl.variety.service.StorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1/user/profile/image")
@RequiredArgsConstructor
public class ProfileImageController {

	private final StorageService storageService;

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<HttpStatus> profileImageUploader(
			@RequestPart(name = "file", required = true) final MultipartFile file) {
		return ResponseEntity.status(storageService.save(file)).build();
	}

	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<InputStreamResource> retreiveCurrentProfileImage() {
		final var currentProfileImage = storageService.retreive();
		final var objectContent = new InputStreamResource(currentProfileImage.getObjectContent());
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment;filename=" + currentProfileImage.getObjectMetadata().getContentDisposition())
				.body(objectContent);
	}

	@GetMapping(value = "/version")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<List<S3VersionSummary>> retreiveAllProfileImageVersions() {
		return ResponseEntity.status(HttpStatus.OK).body(storageService.retrieveVersions());
	}

	@PutMapping(value = "/version/{versionId}")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<HttpStatus> setPreviousVersionAsCurrentProfileImage(
			@PathVariable(name = "versionId") final String versionId) {
		return null;
	}

	@DeleteMapping(value = "/version/{versionId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public ResponseEntity<HttpStatus> deleteProfileImageVersion(
			@PathVariable(name = "versionId") final String versionId) {
		storageService.deleteVersion(versionId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
