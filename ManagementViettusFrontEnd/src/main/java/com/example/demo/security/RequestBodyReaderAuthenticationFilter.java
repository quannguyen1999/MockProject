package com.example.demo.security;

import com.example.demo.model.CustomResponse;
import com.example.demo.model.ResponseToken;
import com.example.demo.service.AccountService;
import com.example.demo.service.ConvertService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//custom when login
public class RequestBodyReaderAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	//to find username
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ConvertService convertService;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		//get username from login then set to this
		UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);
		setDetails(request, authRequest);
		
		//get response token from server
		CustomResponse customResponse = accountService.signIn(obtainUsername(request), obtainPassword(request));
		ResponseToken responseToken = convertService.convertToObject(customResponse, new ResponseToken());
		//if return is null then throw error
		if(customResponse.getListErrorMessages() != null || responseToken == null) {
			HttpSession session = request.getSession();
			session.setAttribute("error", customResponse.getListErrorMessages().get(0).getMessage());
			throw new UsernameNotFoundException("Username invalid");
		}else {
			//otherwise set token and typeAccount to session
			HttpSession session = request.getSession();
			session.setAttribute("token", responseToken.getToken());
			session.setAttribute("typeAccount", responseToken.getTypeAccount().toString());
			return this.getAuthenticationManager()
					.authenticate(authRequest);
		}
	}

	private UsernamePasswordAuthenticationToken getAuthRequest(
			HttpServletRequest request) {
		String username = obtainUsername(request);
		return new UsernamePasswordAuthenticationToken(
				username, "Demo123@");
	}

}
