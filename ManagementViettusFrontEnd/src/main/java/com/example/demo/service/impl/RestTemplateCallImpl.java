package com.example.demo.service.impl;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.example.demo.model.CustomResponse;
import com.example.demo.service.RestTemplateCallService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestTemplateCallImpl implements RestTemplateCallService{

	@Autowired
	private RestTemplate restTemplate;

	private CustomResponse getCustomeResponse(String url, HttpMethod httpMethod, HttpEntity<?> entity) {
		CustomResponse customResponse = null;
		try {
			customResponse = restTemplate.exchange(
					url,
					httpMethod,
					entity,
					CustomResponse.class).getBody();
		} catch(HttpStatusCodeException e) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				customResponse = objectMapper.readValue(e.getResponseBodyAsString(), CustomResponse.class);
			} catch (JsonParseException e1) {
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return customResponse;
	}
	
	@Override
	public <T> CustomResponse getResponse(String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		CustomResponse customResponse = null;
		final HttpEntity<String> entity = new HttpEntity<String>(headers);
		customResponse =  getCustomeResponse(url, HttpMethod.GET, entity);
		return customResponse;
	}

	@Override
	public <T> CustomResponse getResponseWithToken(String url, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("AccessToken", token);
		final HttpEntity<String> entity = new HttpEntity<String>(headers);
		return  getCustomeResponse(url, HttpMethod.GET, entity);
	}

	@Override
	public <T> CustomResponse postResponse(Object object, String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Object> entity = new HttpEntity<Object>(object,headers);
		return  getCustomeResponse(url, HttpMethod.POST, entity);
	}

	@Override
	public CustomResponse postResponseWithToken(Object object, String url, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("AccessToken", token);
		HttpEntity<Object> entity = new HttpEntity<Object>(object,headers);
		return  getCustomeResponse(url, HttpMethod.POST, entity);
	}

	@Override
	public CustomResponse PutResponseWithToken(Object object, String url, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("AccessToken", token);
		HttpEntity<Object> entity = new HttpEntity<Object>(object,headers);
		return  getCustomeResponse(url, HttpMethod.PUT, entity);
	}

}