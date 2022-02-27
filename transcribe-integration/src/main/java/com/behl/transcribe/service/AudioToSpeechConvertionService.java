package com.behl.transcribe.service;

import java.io.InputStream;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.behl.transcribe.properties.AwsS3ConfigurationProperties;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@EnableConfigurationProperties(AwsS3ConfigurationProperties.class)
public class AudioToSpeechConvertionService {

	private final StorageService storageService;
	private final AmazonTranscribe amazonTranscribe;
	private final AwsS3ConfigurationProperties awsS3ConfigurationProperties;

	public InputStream convert(final MultipartFile file, final String languageCode) {
		// Save audio file provided by user to configured input S3 Bucket
		storageService.save(file);

		// Prepare a Media object for the above saved file
		final var media = new Media();
		media.setMediaFileUri(constructS3Uri(file));

		// Preparing a TranscriptionJobRequest
		final String jobName = "hardik";
		StartTranscriptionJobRequest startTranscriptionJobRequest = new StartTranscriptionJobRequest();
		startTranscriptionJobRequest.setLanguageCode(languageCode);
		startTranscriptionJobRequest.setOutputBucketName(awsS3ConfigurationProperties.getS3().getOutputBucketName());
		startTranscriptionJobRequest.setTranscriptionJobName(jobName);
		startTranscriptionJobRequest.setMedia(media);

		amazonTranscribe.startTranscriptionJob(startTranscriptionJobRequest);

		// Poll for output JSON result from configured output S3 bucket
		Boolean resultRetreived = false;
		S3Object result = null;
		while (!resultRetreived) {
			result = storageService.retrieve(jobName);
			if (result != null)
				resultRetreived = true;
		}
		return result.getObjectContent();
	}

	private String constructS3Uri(final MultipartFile file) {
		return "s3://" + awsS3ConfigurationProperties.getS3().getInputBucketName() + "/" + file.getOriginalFilename();
	}

}
