package com.behl.cipherinator.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "UserLoginRequest", accessMode = Schema.AccessMode.READ_ONLY)
public class UserLoginRequestDto {

	@NotBlank(message = "UserName must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, example = "hardik.behl", description = "UserName associated with user record already created in the system")
	private String userName;
	
	@NotBlank(message = "Password must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, example = "SomethingSecure@123", description = "Password corresponding to provided UserName")
	private String password;

}
