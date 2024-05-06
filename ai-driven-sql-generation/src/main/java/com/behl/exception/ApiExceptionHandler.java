package com.behl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResponseStatusException.class)
	public ProblemDetail handle(final ResponseStatusException exception) {
		logException(exception);
		return ProblemDetail.forStatusAndDetail(exception.getStatusCode(), exception.getReason());
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handle(final Exception exception) {
		logException(exception);
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_IMPLEMENTED, "Something went wrong.");
	}

	private void logException(final @NonNull Exception exception) {
		log.error("Exception encountered: {}", exception.getMessage(), exception);
	}

}