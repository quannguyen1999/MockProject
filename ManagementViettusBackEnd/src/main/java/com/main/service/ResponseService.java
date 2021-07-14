package com.main.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.main.model.ErrorMessage;

public interface ResponseService {
	public  ResponseEntity<Object> getResponseBadRequest(String urlMessage,List<ErrorMessage> listErrorMessage);
	
	public  ResponseEntity<Object> getResponseCustom(String urlMessage,Object object, HttpStatus httpStatus);

	public String getMessage(String urlMessage);
}
