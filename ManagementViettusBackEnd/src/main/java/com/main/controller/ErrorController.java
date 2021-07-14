package com.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.main.model.CustomResponse;

//nơi xử lý tất cả error
@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler{
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<CustomResponse> handleAllUncaughtException(
			Exception exception, 
			WebRequest request){
		return new ResponseEntity<>(new CustomResponse(
				null,
				exception.getMessage(),
				null),	
				HttpStatus.OK);
	}
}
