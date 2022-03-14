package com.behl.event.log.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class User {

	private final UUID userId;
	private final String emailId;

}
