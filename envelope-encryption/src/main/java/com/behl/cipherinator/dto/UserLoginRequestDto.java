package com.behl.cipherinator.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class UserLoginRequestDto implements Serializable {

	private static final long serialVersionUID = -7570191085432547398L;

	private final String userName;
	private final String password;

}
