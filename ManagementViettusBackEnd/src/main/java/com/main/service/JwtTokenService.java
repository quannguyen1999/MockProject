package com.main.service;

import java.util.Date;


import com.main.entity.Account;


public interface JwtTokenService {
	//to generate token
	public String generateAccessToken(Account account);
	
	public String getUserName(String token);
	
	public String getTypeAccount(String token);
	
	public Date getExpirationDate(String token);
	
	public String validate(String token);
	
	public String hashPassword(String password);
}
