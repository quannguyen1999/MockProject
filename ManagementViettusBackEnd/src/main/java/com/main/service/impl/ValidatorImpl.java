package com.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.main.model.ErrorMessage;
import com.main.service.ValidatorService;

@Service
public class ValidatorImpl implements ValidatorService{
	@Override
	public List<ErrorMessage> checkBindingResult(BindingResult result){
		List<ErrorMessage> listCustomError = new ArrayList<ErrorMessage>();
		if (result.hasErrors()) {
			for (FieldError error : result.getFieldErrors()) {
				listCustomError.add(new ErrorMessage(error.getField(), error.getDefaultMessage()));
			}
		}
		return listCustomError;
	}
}
