package com.behl.sampler.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.joda.time.LocalDateTime;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.behl.sampler.properties.AmazonS3ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Hardik Singh Behl
 */

@Slf4j
@Service
@AllArgsConstructor
@EnableConfigurationProperties(value = AmazonS3ConfigurationProperties.class)
public class StorageService {

	private final AmazonS3 amazonS3;
	private final AmazonS3ConfigurationProperties amazonS3ConfigurationProperties;

	public URI retreivePresignedUrl(final String key) {
		final var s3Properties = amazonS3ConfigurationProperties.getS3();

		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
				s3Properties.getBucketName(), key, HttpMethod.GET);
		generatePresignedUrlRequest.setExpiration(new LocalDateTime().plusSeconds(10).toDate());

		final URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
		try {
			return presignedUrl.toURI();
		} catch (URISyntaxException e) {
			log.error("Error occured: {}" + e);
			throw new RuntimeException("Unable to retreive URI");
		}
	}
}
