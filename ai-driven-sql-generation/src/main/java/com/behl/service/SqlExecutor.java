package com.behl.service;

import org.springframework.stereotype.Service;

import com.behl.dto.QueryResponseDto;
import com.behl.exception.EmptyResultException;

import jakarta.persistence.EntityManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SqlExecutor {

	private final EntityManager entityManager;

	public QueryResponseDto execute(@NonNull final String query) {
		final var result = entityManager.createNativeQuery(query).getResultList();
		if (result.isEmpty()) {
			throw new EmptyResultException("No results found for the provided query.");
		}
		return new QueryResponseDto(result);
	}

}
