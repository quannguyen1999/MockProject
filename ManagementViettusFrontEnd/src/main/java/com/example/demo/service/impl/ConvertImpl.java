package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.CustomResponse;
import com.example.demo.model.RestResponsePage;
import com.example.demo.service.ConvertService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConvertImpl implements ConvertService{

	@Override
	public <T> RestResponsePage convertToResponsePage(CustomResponse customResponse) {
		if(customResponse == null || customResponse.getData() == null) {
			return null;
		}
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		return objectMapper.convertValue(
				customResponse.getData(), 
				new TypeReference<RestResponsePage<T>>(){}
				);
	}

	@Override
	public <T> List<T> convertToList(CustomResponse customResponse, T t) {
		if(customResponse == null || customResponse.getData() == null) {
			return null;
		}
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		return 
				objectMapper.convertValue(customResponse.getData(),objectMapper.getTypeFactory().constructCollectionLikeType(List.class, t.getClass()));
	}

	@Override
	public <T> T convertToObject(CustomResponse customResponse, T t) {
		if(customResponse == null || customResponse.getData() == null) {
			return null;
		}
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		return (T) objectMapper.convertValue(customResponse.getData(),t.getClass());
	}
}
