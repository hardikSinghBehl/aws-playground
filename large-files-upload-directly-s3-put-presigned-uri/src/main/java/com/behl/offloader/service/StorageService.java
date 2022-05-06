package com.behl.offloader.service;

import java.net.URISyntaxException;
import java.net.URL;

import org.joda.time.LocalDateTime;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.behl.offloader.properties.AwsS3ConfigurationProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AwsS3ConfigurationProperties.class)
public class StorageService {

	private final AmazonS3 amazonS3;
	private final AwsS3ConfigurationProperties awsS3ConfigurationProperties;

	public String generateTemporaryPutUri(final String objectKey) {
		final GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
				awsS3ConfigurationProperties.getS3().getBucketName(), objectKey, HttpMethod.PUT);
		generatePresignedUrlRequest.setExpiration(new LocalDateTime()
				.plusMinutes(awsS3ConfigurationProperties.getS3().getPresignedUriExpiration()).toDate());

		final URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
		try {
			return presignedUrl.toURI().toString();
		} catch (final URISyntaxException exception) {
			log.error("Error occured: {}" + exception);
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
	}

}
