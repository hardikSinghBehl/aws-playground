package com.behl.sticky.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.sticky.properties.OneTimePasswordConfigurationProperties;
import com.behl.sticky.service.OneTimePasswordService;
import com.behl.sticky.utility.CookieFactory;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/v1/otp")
@EnableConfigurationProperties(value = OneTimePasswordConfigurationProperties.class)
public class OtpController {

	private final OneTimePasswordService oneTimePasswordService;
	private final OneTimePasswordConfigurationProperties oneTimePasswordConfigurationProperties;

	@GetMapping(value = "/generate/{emailId}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<Map<String, Object>> generateOtp(@PathVariable(name = "emailId") final String emailId,
			final HttpServletResponse httpServletResponse) {
		final var response = oneTimePasswordService.generate(emailId);
		CookieFactory.insert((oneTimePasswordConfigurationProperties.getOtp().getExpirationMinutes() * 1 * 60) + 60,
				httpServletResponse);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/validate")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<Map<String, String>> validateOtp(@RequestParam(name = "emailId") final String emailId,
			@RequestParam(name = "otp") final Integer otp, final HttpServletResponse httpServletResponse) {
		final var response = oneTimePasswordService.validate(emailId, otp);
		if (response.get("status").equalsIgnoreCase("SUCCESS"))
			CookieFactory.remove(httpServletResponse);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}