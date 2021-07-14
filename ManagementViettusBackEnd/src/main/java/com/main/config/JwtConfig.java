package com.main.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//get value in properties
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {
	//Get secret and expired of token
	private String secretKey;
	private String tokenPrefix;
	private String tokenExpirationAfterDays;
	
	private String clientId;
	private String privateKey;
	private String publicKey;
}