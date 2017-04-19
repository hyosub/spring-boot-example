package com.h4d1.exception;

import com.h4d1.exception.type.ServiceError;

public class ServiceException extends Exception {
	private final ServiceError error;
	private String detailErrorMessage;
	
	/** 시리얼 버전 UID */
	private static final long serialVersionUID = -713453265263906921L;
	
	public ServiceException(ServiceError error) {
		this.error = error;
	}
	
	public ServiceException(ServiceError error, String detailErrorMessage) {
		this.error = error;
		this.detailErrorMessage = detailErrorMessage;
	}

	public ServiceError getError() {
		return error;
	}

	public int getErrorCode() {
		return error.getCode();
	}
	
	public String getErrorMessage() {
		return error.getMessage();
	}
	
	public String getDetailErrorMessage() {
		return detailErrorMessage;
	}

	public void setDetailErrorMessage(String detailErrorMessage) {
		this.detailErrorMessage = detailErrorMessage;
	}
}
