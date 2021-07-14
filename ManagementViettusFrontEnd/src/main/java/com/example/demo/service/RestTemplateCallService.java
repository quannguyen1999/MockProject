package com.example.demo.service;


import com.example.demo.model.CustomResponse;

public interface RestTemplateCallService {
	//get response with auto custom response
	public <T> CustomResponse getResponse(String url);

	//get response with token
	public <T> CustomResponse getResponseWithToken(String url, String token);

	//get response token
	public <T> CustomResponse postResponse(Object object, String url);

	//post request
	public CustomResponse postResponseWithToken(Object object, String url, String token);

	//put request
	public CustomResponse PutResponseWithToken(Object object, String url, String token);
}
