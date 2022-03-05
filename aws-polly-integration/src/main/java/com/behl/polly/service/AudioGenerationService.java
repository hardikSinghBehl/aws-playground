package com.behl.polly.service;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.Voice;

import lombok.AllArgsConstructor;

/**
 * @author Hardik Singh Behl
 * @email behl.hardiksingh@gmail.com
 */

@Service
@AllArgsConstructor
public class AudioGenerationService {

	private final AmazonPolly amazonPolly;

	public InputStream generate(final String inputText) {
		final var synthesizeSpeechRequest = new SynthesizeSpeechRequest().withText(inputText)
				.withVoiceId(getDefaultVoice().getId()).withOutputFormat(OutputFormat.Mp3);

		final var synthesizeSpeechResult = amazonPolly.synthesizeSpeech(synthesizeSpeechRequest);

		return synthesizeSpeechResult.getAudioStream();
	}

	/**
	 * A random voice is configured to be used with each API call, this can be
	 * hard-coded based on requirement and preference
	 */
	private Voice getDefaultVoice() {
		List<Voice> voices = amazonPolly.describeVoices(new DescribeVoicesRequest()).getVoices();
		return voices.get(new Random().nextInt(voices.size()));
	}

}
