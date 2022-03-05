package com.behl.polly.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class TextToAudioConversionRequest {

	private final String text;

}
