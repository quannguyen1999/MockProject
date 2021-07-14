package com.example.demo.service;

import java.util.List;

import com.example.demo.model.CustomResponse;
import com.example.demo.model.RestResponsePage;

public interface ConvertService {
	@SuppressWarnings("rawtypes")
	<T> RestResponsePage convertToResponsePage(CustomResponse customResponse);
	
	<T> List<T> convertToList(CustomResponse customResponse, T t);
	
	<T> T convertToObject(CustomResponse customResponse, T t);
}
