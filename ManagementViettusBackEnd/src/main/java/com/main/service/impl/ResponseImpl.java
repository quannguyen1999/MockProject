package com.main.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.main.model.CustomResponse;
import com.main.model.ErrorMessage;
import com.main.service.ResponseService;

@Service
public class ResponseImpl implements ResponseService{
	//để lấy message từ file message_url.properties
	@Autowired
	private MessageSource messageSource;

	@Override
	public  ResponseEntity<Object> getResponseBadRequest(String urlMessage,List<ErrorMessage> listErrorMessage){
		return new ResponseEntity<>(new CustomResponse(
				listErrorMessage,
				getMessage(urlMessage),null)
				,HttpStatus.BAD_REQUEST);
	}

	@Override
	public  ResponseEntity<Object> getResponseCustom(String urlMessage,Object object, HttpStatus httpStatus){
		return new ResponseEntity<>(new CustomResponse(
				null,
				getMessage(urlMessage),object)
				,httpStatus);
	}

	@Override
	public String getMessage(String urlMessage) {
		return messageSource.getMessage(urlMessage,null, Locale.ENGLISH);
	}
}
