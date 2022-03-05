package com.behl.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class UserCreationRequestDto {

	private final String emailId;
	private final String password;

}
