package com.behl.cipherinator.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class UserCreationRequestDto implements Serializable {

	private static final long serialVersionUID = 3253393359316477118L;

	private final String userName;
	private final String password;

}
