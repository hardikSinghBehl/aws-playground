package com.behl.service;

import java.util.Map;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import com.behl.dto.QueryRequestDto;
import com.behl.exception.InvalidQueryException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SqlGenerator {

	private final ChatClient chatClient;
	private final PromptTemplate promptTemplate;

	public String generate(@NonNull final QueryRequestDto queryRequestDto) {
		final var prompt = promptTemplate.create(Map.of("question", queryRequestDto.question()));
		final var response = chatClient.call(prompt).getResult().getOutput().getContent();

		final var isSelectQuery = response.startsWith("SELECT");
		if (Boolean.FALSE.equals(isSelectQuery)) {
			throw new InvalidQueryException(response);
		}

		return response;
	}

}
