package com.behl.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.behl.configuration.UserAccountProperties;
import com.behl.dto.AccountDeactivationRequestDto;
import com.behl.dto.CancelAccountDeactivationRequestDto;
import com.behl.utility.ScheduleExpressionGenerator;
import com.behl.utility.ScheduleNameGenerator;
import com.behl.utility.ScheduleRegistrar;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(UserAccountProperties.class)
public class UserAccountService {

	private final ScheduleRegistrar scheduleRegistrar;
	private final UserAccountProperties userAccountProperties;
	private final ScheduleNameGenerator scheduleNameGenerator;
	private final ScheduleExpressionGenerator scheduleExpressionGenerator;

	public void deactivateAccount(@NonNull final AccountDeactivationRequestDto request) {
		final var scheduleName = scheduleNameGenerator.generate(request.emailId());

		final var deactivationDelay = userAccountProperties.getDeactivationDelay();
		final var deactivationDateTime = LocalDateTime.now(ZoneOffset.UTC).plus(deactivationDelay);
		final var scheduleExpression = scheduleExpressionGenerator.generate(deactivationDateTime);

		scheduleRegistrar.register(scheduleName, scheduleExpression, request);
	}

	public void cancelAccountDeactivation(@NonNull final CancelAccountDeactivationRequestDto request) {
		final var scheduleName = scheduleNameGenerator.generate(request.emailId());
		scheduleRegistrar.delete(scheduleName);
	}

}
