package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.example.demo.model.AccountGetDto;
import com.example.demo.model.AccountPostDto;
import com.example.demo.model.CustomResponse;
import com.example.demo.service.AccountService;
import com.example.demo.service.RestTemplateCallService;

@Service
@PropertySource("classpath:url.properties")
public class AccountImpl implements AccountService{

	//cal service restemplate
	@Autowired
	private RestTemplateCallService restTemplateCallService;
	
	//call url from file env
	@Autowired
	private Environment env;

	//get type account from url
	@Override
	public CustomResponse getResponseToken(String username) {
		return restTemplateCallService.postResponse(new AccountGetDto(username),env.getProperty("url.localhost")+ env.getProperty("url.getTypeAccount"));
	}

	//get token from base on password and username
	@Override
	public CustomResponse signIn(String username, String password) {
		System.out.println(username);
		System.out.println(password);
		return restTemplateCallService.postResponse(new AccountPostDto(username, password),env.getProperty("url.localhost")+ env.getProperty("url.signin"));
	}

	//get list account 
	@Override
	public CustomResponse getListAccount(String token) {
		CustomResponse customResponse = restTemplateCallService.getResponseWithToken(env.getProperty("url.localhost")+ env.getProperty("url.list"), token);
		return customResponse;
	}

	//get list account but pagination list
	@Override
	public CustomResponse getListAccount(String token, int page, int size) {
		return restTemplateCallService.getResponseWithToken(env.getProperty("url.localhost")+ env.getProperty("url.listPagination")+"?pageSize="+size+"&pageNo="+page, token);
	}

	@Override
	public CustomResponse findAll(String token, String urlRequestParam) {
		return restTemplateCallService.getResponseWithToken(env.getProperty("url.localhost")+ env.getProperty("url.findAll")+urlRequestParam, token);
	}
		
}
