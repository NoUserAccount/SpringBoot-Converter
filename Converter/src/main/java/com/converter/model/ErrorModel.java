package com.converter.model;

import org.springframework.stereotype.Component;

@Component
public class ErrorModel {

	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
