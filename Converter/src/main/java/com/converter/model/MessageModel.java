package com.converter.model;

import org.springframework.stereotype.Component;

@Component
public class MessageModel {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
