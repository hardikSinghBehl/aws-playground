package com.behl.sqs.consumer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@ToString
public class SuperHeroDto {

	private final String descriptor;
	private final String name;
	private final String power;
	private final String suffix;
	private final String prefix;

}
