package com.behl.variety.controller;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.S3VersionSummary;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1/user/profile/image")
@RequiredArgsConstructor
public class ProfileImageController {

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<HttpStatus> profileImageUploader() {
		return null;
	}

	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<InputStreamResource> retreiveCurrentProfileImage() {
		return null;
	}

	@GetMapping(value = "/version")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<List<S3VersionSummary>> retreiveAllProfileImageVersions() {
		return null;
	}

	@GetMapping(value = "/version")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<HttpStatus> setPreviousVersionAsCurrentProfileImage() {
		return null;
	}

	@DeleteMapping(value = "/version/{versionId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public ResponseEntity<HttpStatus> deleteProfileImageVersion(
			@PathVariable(name = "versionId") final String versionId) {
		return null;
	}

}
