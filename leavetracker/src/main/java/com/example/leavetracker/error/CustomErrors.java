package com.example.leavetracker.error;

import org.springframework.stereotype.Component;


public class CustomErrors {
	
	private int statusCode;
	private String message;
	
	public void setCode(int code) {
		this.statusCode = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CustomErrors(int statusCode, String message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}

	public int getCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}
	
	
}
