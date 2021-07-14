package com.main.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.main.entity.Account;
import com.main.model.AccountCreateDto;
import com.main.model.AccountGetDto;
import com.main.model.AccountPostDto;
import com.main.model.ErrorMessage;
import com.main.service.AccountService;
import com.main.service.JwtTokenService;
import com.main.service.ResponseService;
import com.main.service.ValidatorService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class AccountValidator{

	private final AccountService accountService;
	
	private static final String FIELD_USERNAME = "username";
	
	private final JwtTokenService jwtTokenService;
	
	private final ValidatorService validatorService;
	
	private final ResponseService responseService;
	
	public List<ErrorMessage> validateDeleteAccount(String username, 
			String token) {
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		if(accountService.findByUserName(username)==null) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME, 
					responseService.getMessage("account.username.notFound")));
		}
		String usernameFromToken = jwtTokenService.getUserName(token);
		if(username.equalsIgnoreCase("admin")) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME,
					responseService.getMessage("account.username.canDeleteAdmin")));
		}else if(usernameFromToken.equalsIgnoreCase(username)) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME,
					responseService.getMessage("account.username.canDeleteYourSelf")));
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateUpdateAccount(String username, 
			String token) {
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		
		if(accountService.findByUserName(username)==null) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME,
					responseService.getMessage("account.username.notFound")));
		}
		
		String usernameFromToken = jwtTokenService.getUserName(token);
		
		if(username.equalsIgnoreCase("admin")) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME,
					responseService.getMessage("account.username.canUpdateAdmin")));
		}else if(usernameFromToken.equalsIgnoreCase(username)) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME,
					responseService.getMessage("account.username.canUpdateYourSelf")));
		}
		return listErrorResponses;
	}
	
	
	
	public List<ErrorMessage> validateSignUpAccount(AccountPostDto accountPostDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = validatorService.checkBindingResult(result);
		if(listErrorResponses.size()>=1) {
			return listErrorResponses;
		}
		if(accountService.findByUserName(accountPostDto.getUserName())!=null) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME,
					responseService.getMessage("account.username.conflix")));
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateCreateAccount(AccountCreateDto accountCreateDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = validatorService.checkBindingResult(result);
		if(listErrorResponses.size()>=1) {
			return listErrorResponses;
		}
		if(accountService.findByUserName(accountCreateDto.getUserName())!=null) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME,
					responseService.getMessage("account.username.conflix")));
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateSignInAccount(AccountPostDto accountPostDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = validatorService.checkBindingResult(result);
		if(listErrorResponses.size()>=1) {
			return listErrorResponses;
		}
		Account account = accountService.findByUserNameAndReturnAccount(accountPostDto.getUserName());
		if(account == null) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME,
					responseService.getMessage("account.username.notFound")));
		}else if(accountService.comparePassword( accountPostDto.getPassword(),account.getPassword()) == false) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME,
					responseService.getMessage("account.password.incorrect")));
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateGetTypeAccount(AccountGetDto accountGetDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = validatorService.checkBindingResult(result);
		if(listErrorResponses.size()>=1) {
			return listErrorResponses;
		}
		if(accountService.findByUserName(accountGetDto.getUserName())==null) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME,
					responseService.getMessage("account.username.notFound")));
		}
		return listErrorResponses;
	}
	
}
