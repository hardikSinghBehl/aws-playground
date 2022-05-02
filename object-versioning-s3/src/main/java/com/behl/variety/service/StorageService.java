package com.behl.variety.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteVersionRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
import com.behl.variety.properties.AwsS3ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@EnableConfigurationProperties(value = AwsS3ConfigurationProperties.class)
@Slf4j
public class StorageService {

	/**
	 * In a real application, the key has to be unique for each user for which
	 * multiple versions of the profile-image are to be stored, the prefix can be
	 * kept as a value that is unique for each user and unlikely to change, like a
	 * UUID primary-id in the DB, the latter part of the key can be kept as static
	 */
	private static final String KEY_NAME = "user-primary-id/profile-image";

	private final AmazonS3 amazonS3;
	private final AwsS3ConfigurationProperties awsS3ConfigurationProperties;

	/**
	 * 
	 * @param file: represents an object to be saved in configured S3 Bucket, the
	 *              saved object will be the active version of the users profile
	 *              picture
	 * @return HttpStatus 200 OK if file was saved, HttpStatus 417
	 *         EXPECTATION_FAILED if file was not saved.
	 */
	public HttpStatus save(final MultipartFile file) {
		ObjectMetadata metadata = constructMetadata(file);

		try {
			final var putObjectRequest = new PutObjectRequest(awsS3ConfigurationProperties.getS3().getBucketName(),
					KEY_NAME, file.getInputStream(), metadata);
			amazonS3.putObject(putObjectRequest);
		} catch (final SdkClientException | IOException exception) {
			log.error("UNABLE TO STORE {} IN S3: {} ", file.getOriginalFilename(), LocalDateTime.now(), exception);
			return HttpStatus.EXPECTATION_FAILED;
		}
		return HttpStatus.CREATED;
	}

	/**
	 * Retrieves the current profile image version for the user from the S3 Bucket
	 * 
	 * @return object of S3Object.class corresponding to the latest profile image
	 *         version
	 */
	public S3Object retreive() {
		final var getObjectRequest = new GetObjectRequest(awsS3ConfigurationProperties.getS3().getBucketName(),
				KEY_NAME);
		return amazonS3.getObject(getObjectRequest);
	}

	/**
	 * Method to return list of all profile image versions
	 * 
	 * @return S3Object representing the transcribed result
	 */
	public List<S3VersionSummary> retrieveVersions() {
		final ListVersionsRequest request = new ListVersionsRequest()
				.withBucketName(awsS3ConfigurationProperties.getS3().getBucketName()).withPrefix(KEY_NAME);
		final VersionListing versionListing = amazonS3.listVersions(request);
		return versionListing.getVersionSummaries();
	}

	/**
	 * Method to delete specific profile image version
	 * 
	 * @param versionId corresponding to the object version to delete
	 */
	public void deleteVersion(final String versionId) {
		final var deleteVersionRequest = new DeleteVersionRequest(awsS3ConfigurationProperties.getS3().getBucketName(),
				KEY_NAME, versionId);
		amazonS3.deleteVersion(deleteVersionRequest);
	}

	private ObjectMetadata constructMetadata(final MultipartFile file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.addUserMetadata("object_id", UUID.randomUUID().toString());
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());
		metadata.setContentDisposition(file.getOriginalFilename());
		return metadata;
	}

}