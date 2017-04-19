package com.h4d1.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.h4d1.exception.ServiceException;
import com.h4d1.util.json.Error;
import com.h4d1.util.json.response.ErrorResponse;

@ControllerAdvice
public class StandardExceptionHandler {
	
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public ErrorResponse handleServiceException(HttpServletRequest request, ServiceException serviceException) {
		String requestUri = request.getRequestURI();
		String details = serviceException.getDetailErrorMessage();

		Error error = new Error(serviceException.getErrorCode(), serviceException.getErrorMessage());
		
		if (details != null) {
			error.setDetails(details);
		}
		
		return new ErrorResponse(requestUri, error);
	}
}
