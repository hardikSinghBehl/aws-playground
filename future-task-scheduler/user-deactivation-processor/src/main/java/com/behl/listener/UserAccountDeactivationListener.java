package com.behl.listener;

import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;
import com.behl.dto.AccountDeactivationRequestDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserAccountDeactivationListener {

	@SqsListener("${com.behl.aws.sqs.queue-url}")
	public void listen(final AccountDeactivationRequestDto accountDeactivationRequest) {
		log.info("Deactivating user account {}", accountDeactivationRequest.emailId());
		// business logic
	}

}
