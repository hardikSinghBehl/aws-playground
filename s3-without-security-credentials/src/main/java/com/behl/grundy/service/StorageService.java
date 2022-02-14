package com.behl.grundy.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.behl.grundy.properties.AwsProperties;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Hardik Singh Behl
 */

@Service
@AllArgsConstructor
@EnableConfigurationProperties(value = AwsProperties.class)
@Slf4j
public class StorageService {

	private final AmazonS3 amazonS3;
	private final AwsProperties awsProperties;

	/**
	 * 
	 * @param file: represents an object to be saved in configured S3 Bucket
	 * @return HttpStatus 200 OK if file was saved, HttpStatus 417
	 *         EXPECTATION_FAILED if file was not saved.
	 */
	public HttpStatus save(final MultipartFile file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());
		metadata.setContentDisposition(file.getOriginalFilename());

		try {
			amazonS3.putObject(awsProperties.getS3().getBucketName(), file.getOriginalFilename(), file.getInputStream(),
					metadata);
		} catch (SdkClientException | IOException e) {
			log.error("UNABLE TO STORE {} IN S3: {} ", file.getOriginalFilename(), LocalDateTime.now());
			return HttpStatus.EXPECTATION_FAILED;
		}
		return HttpStatus.OK;
	}

	/**
	 * @return List of object keys in the configured S3 Bucket
	 */
	public List<String> listFiles() {
		return amazonS3.listObjects(awsProperties.getS3().getBucketName()).getObjectSummaries().parallelStream()
				.map(s3Object -> s3Object.getKey()).collect(Collectors.toList());
	}

}
