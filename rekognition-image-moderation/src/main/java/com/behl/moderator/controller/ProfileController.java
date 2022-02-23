package com.behl.moderator.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.behl.moderator.service.ImageModerationService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ProfileController {

	private final ImageModerationService imageModerationService;

	@PutMapping(value = "/users/profile/image")
	public ResponseEntity<?> updateProfilePicture(
			@RequestPart(name = "file", required = true) final MultipartFile profileImage) {
		final var moderationLabels = imageModerationService.moderate(profileImage);

		if (moderationLabels != null && moderationLabels.size() > 0) {

			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
					.body("Our Systems Detected that the image may contain " + String.join(", ", moderationLabels
							.parallelStream().map(moderationLabel -> moderationLabel.getName())
							.collect(Collectors.toList())
							+ ". Therefore we are unable to update your profile image at this time, Kindly raise a help desk ticket if you feel this was shown in error"));
		}

		// update users profile image and return success response
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
