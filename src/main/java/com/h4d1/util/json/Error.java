package com.h4d1.util.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class Error {
	private final int code;
	private final String message;
	
	@JsonInclude(Include.NON_NULL)
	private String details;
	
	public Error(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public Error(int code, String message, String details) {
		this.code = code;
		this.message = message;
		this.details = details;
	}
}
