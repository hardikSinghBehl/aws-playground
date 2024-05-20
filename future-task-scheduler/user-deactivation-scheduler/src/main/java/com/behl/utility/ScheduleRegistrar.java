package com.behl.utility;

import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.ActionAfterCompletion;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindowMode;

@Component
@RequiredArgsConstructor
public class ScheduleRegistrar {

	private final TargetBuilder targetBuilder;
	private final SchedulerClient schedulerClient;

	public void register(@NonNull final String scheduleName, 
			@NonNull final String scheduleExpression, 
			@NonNull final Object input) {
		final var target = targetBuilder.build(input);
		
		schedulerClient.createSchedule(schedule -> schedule
				.name(scheduleName)
				.scheduleExpression(scheduleExpression)
				.actionAfterCompletion(ActionAfterCompletion.DELETE)
				.target(target)
				.flexibleTimeWindow(flexibleTimeWindow -> flexibleTimeWindow
						.mode(FlexibleTimeWindowMode.OFF))
				.build());
	}
	
	public void delete(@NonNull final String scheduleName) {
		schedulerClient.deleteSchedule(schedule -> schedule.name(scheduleName).build());
	}

}
