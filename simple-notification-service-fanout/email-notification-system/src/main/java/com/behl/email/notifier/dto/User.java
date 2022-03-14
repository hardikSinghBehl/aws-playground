package com.behl.email.notifier.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class User {

	private final String emailId;
	private final UUID userId;

}
