package com.behl.cipherinator.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "UserCreationRequest", accessMode = Schema.AccessMode.READ_ONLY)
public class UserCreationRequestDto {

	@NotBlank(message = "UserName must not be empty")
	@Size(min = 3, max = 20, message = "UserName length must be between 3 and 20 characters")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Unique UserName for user record", example = "hardik.behl", minLength = 3, maxLength = 10)
	private String userName;
	
	@NotBlank(message = "Password must not be empty")
	@Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Secure Password to enable user login", example = "SomethingSecure@123", minLength = 3, maxLength = 20)
	private String password;

}
