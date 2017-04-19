package com.h4d1.util.json.response;

import java.util.Date;

import com.h4d1.util.json.Error;

import lombok.Data;

@Data
public class ErrorResponse {
	private final String path;
	private final Date timestamp;
	private final Error error;
	
	public ErrorResponse(String path, Error error) {
		this.path = path;
		this.timestamp = new Date();
		this.error = error;
	}
}
