package com.example.demo.service;


import com.example.demo.model.CustomResponse;

public interface AccountService {
	//ResponseToken
	CustomResponse getResponseToken(String username);

	//ResponesToken
	CustomResponse signIn(String username, String password);

	//List<AccountGetDto>
	CustomResponse getListAccount(String token);

	//Page<AccountGetDto>
	CustomResponse getListAccount(String token, int page, int size);

	//Page<AccountGetDto> version 2
	CustomResponse findAll(String token, String urlRequestParam);
}
