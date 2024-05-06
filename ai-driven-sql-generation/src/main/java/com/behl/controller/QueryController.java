package com.behl.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.behl.dto.QueryRequestDto;
import com.behl.dto.QueryResponseDto;
import com.behl.service.SqlExecutor;
import com.behl.service.SqlGenerator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/query")
public class QueryController {

	private final SqlExecutor sqlExecutor;
	private final SqlGenerator sqlGenerator;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QueryResponseDto> executeQuery(@RequestBody @Valid QueryRequestDto queryRequestDto) {
		final var query = sqlGenerator.generate(queryRequestDto);
		final var response = sqlExecutor.execute(query);
		return ResponseEntity.ok(response);
	}

}