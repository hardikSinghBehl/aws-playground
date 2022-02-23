package com.behl.moderator.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsRequest;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.ModerationLabel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ImageModerationService {

	private final AmazonRekognition amazonRekognition;

	public List<ModerationLabel> moderate(final MultipartFile file) {
		DetectModerationLabelsRequest request = new DetectModerationLabelsRequest();
		try {
			request = request.withImage(new Image().withBytes(ByteBuffer.wrap(file.getBytes())));
		} catch (final IOException exception) {
			log.error("Exception occured while moderating file: {} at {}", file.getOriginalFilename(),
					LocalDateTime.now());
			throw new RuntimeException("Unable to moderate given file", exception);
		}

		return amazonRekognition.detectModerationLabels(request).getModerationLabels();
	}

}
