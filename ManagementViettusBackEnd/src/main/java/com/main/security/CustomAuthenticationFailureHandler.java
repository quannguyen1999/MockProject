package com.main.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

//to custom error failure when login failed
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler{
	static String LOGIN_ERROR = "invalid username and password";
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String redirectURL = "/login?error";
		exception = new SessionAuthenticationException(LOGIN_ERROR);
		super.setDefaultFailureUrl(redirectURL);
		super.onAuthenticationFailure(request, response, exception);
	}

}