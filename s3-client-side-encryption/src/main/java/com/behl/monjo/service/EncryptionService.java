package com.behl.monjo.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncryptionService {

	private final AwsCrypto crypto = AwsCrypto.standard();
	private final KmsMasterKeyProvider kmsMasterKeyProvider;

	public byte[] encrypt(final MultipartFile file) {
		final Map<String, String> context = Collections.singletonMap("fileName", file.getOriginalFilename());

		CryptoResult<byte[], KmsMasterKey> encryptResult;
		try {
			encryptResult = crypto.encryptData(kmsMasterKeyProvider, IOUtils.toString(file.getInputStream()).getBytes(),
					context);
		} catch (final IOException exception) {
			log.error("Exception occurred during data encryption", exception);
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return encryptResult.getResult();
	}

	public S3Object decrypt(final S3Object s3Object) {
		CryptoResult<byte[], KmsMasterKey> decryptResult;
		try {
			decryptResult = crypto.decryptData(kmsMasterKeyProvider, s3Object.getObjectContent().readAllBytes());
		} catch (final IOException exception) {
			log.error("Exception occurred during data decryption", exception);
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		s3Object.setObjectContent(new ByteArrayInputStream(new String(decryptResult.getResult()).getBytes()));
		return s3Object;
	}

}
