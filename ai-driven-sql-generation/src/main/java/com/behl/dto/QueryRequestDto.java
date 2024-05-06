package com.behl.dto;

import jakarta.validation.constraints.NotBlank;

public record QueryRequestDto(@NotBlank(message = "question must not be empty") String question) {
}