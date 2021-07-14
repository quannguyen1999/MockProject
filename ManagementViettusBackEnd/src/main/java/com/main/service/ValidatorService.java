package com.main.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.main.model.ErrorMessage;

public interface ValidatorService {
	//check all field and get error from spring validator and return to List model Error Messages
	public List<ErrorMessage> checkBindingResult(BindingResult result);
}
