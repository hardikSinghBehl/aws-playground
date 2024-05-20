package com.behl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AccountDeactivationRequestDto(
	
	@NotBlank(message = "emailId must not be empty") 
	@Email(message = "emailId must be a valid format") 
	String emailId
	
) {
}