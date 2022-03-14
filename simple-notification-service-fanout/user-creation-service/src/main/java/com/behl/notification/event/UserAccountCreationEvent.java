package com.behl.notification.event;

import org.springframework.context.ApplicationEvent;

public class UserAccountCreationEvent extends ApplicationEvent {

	private static final long serialVersionUID = -3525820652888381887L;

	public UserAccountCreationEvent(Object source) {
		super(source);
	}

}