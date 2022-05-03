package com.behl.chubby.service;

import java.io.IOException;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.behl.chubby.properties.AwsS3ConfigurationProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AwsS3ConfigurationProperties.class)
public class StorageService {

	private final AwsS3ConfigurationProperties awsS3ConfigurationProperties;
	private final AmazonS3 amazonS3;
	private final TransferManager transferManager;

	/**
	 * Method to save file in configured S3 bucket
	 * 
	 * @param file: represents an object to be saved in configured S3 Bucket. If
	 *              file size is greater than the configured size in
	 *              `com.behl.aws.s3.multipart-object-size` property, then the file
	 *              will be uploaded in parts of the same configured size
	 * 
	 * @return HttpStatus 200 OK if file was saved
	 */
	public HttpStatus save(final MultipartFile file) {
		ObjectMetadata metadata = constructMetadata(file);
		PutObjectRequest putObjectRequest = constructPutObjectRequest(file, metadata);
		transferManager.upload(putObjectRequest);
		return HttpStatus.CREATED;
	}

	/**
	 * Method to retrieve object from configured S3 Bucket using it's key
	 * 
	 * @param key of S3 Object to retrieve
	 * @return S3Object corresponding to provided key
	 */
	public S3Object retreive(final String key) {
		final var getObjectRequest = new GetObjectRequest(awsS3ConfigurationProperties.getS3().getBucketName(), key);
		return amazonS3.getObject(getObjectRequest);
	}

	private PutObjectRequest constructPutObjectRequest(final MultipartFile file, final ObjectMetadata metadata) {
		PutObjectRequest putObjectRequest;
		try {
			putObjectRequest = new PutObjectRequest(awsS3ConfigurationProperties.getS3().getBucketName(),
					file.getOriginalFilename(), file.getInputStream(), metadata);
		} catch (final IOException exception) {
			log.error("Exception occurred fetching file content", exception);
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
		return putObjectRequest;
	}

	private ObjectMetadata constructMetadata(final MultipartFile file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());
		metadata.setContentDisposition(file.getOriginalFilename());
		return metadata;
	}

}
