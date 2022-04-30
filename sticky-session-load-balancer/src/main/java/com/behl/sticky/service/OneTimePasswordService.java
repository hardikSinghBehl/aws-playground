package com.behl.sticky.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.cache.LoadingCache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OneTimePasswordService {

	private final LoadingCache<String, Integer> oneTimePasswordCache;

	public Map<String, Object> generate(final String emailId) {
		final var response = new HashMap<String, Object>();

		try {
			if (oneTimePasswordCache.get(emailId) != null)
				oneTimePasswordCache.invalidate(emailId);
		} catch (ExecutionException exception) {
			log.error("Unable to retreive key from OTP cache", exception);
		}

		final var otp = new Random().ints(1, 100000, 999999).sum();
		oneTimePasswordCache.put(emailId, otp);

		response.put("otp", otp);
		response.put("emailId", emailId);
		response.put("status", "SUCCESS");
		response.put("timestamp", LocalDateTime.now().toString());
		return response;
	}

	public Map<String, String> validate(final String emailId, final Integer otp) {
		final var response = new HashMap<String, String>();
		Boolean otpIsCorrect;
		try {
			otpIsCorrect = oneTimePasswordCache.get(emailId).equals(otp);
		} catch (ExecutionException exception) {
			log.error("Unable to validate OTP", exception);
			throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
		}
		if (otpIsCorrect) {
			response.put("status", "SUCCESS");
			response.put("message", "Validation Successfull, OTP entered was correct.");
		} else {
			response.put("status", "FAILURE");
			response.put("message", "Validation Unsuccessfull, OTP entered was Incorrect.");
		}

		response.put("timestamp", LocalDateTime.now().toString());
		return response;
	}
}