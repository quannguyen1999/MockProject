package com.main.controller;

import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.main.entity.Account;
import com.main.model.AccountCreateDto;
import com.main.model.AccountGetDto;
import com.main.model.AccountPostDto;
import com.main.model.AccountUpdateDto;
import com.main.model.ErrorMessage;
import com.main.service.AccountService;
import com.main.service.JwtTokenService;
import com.main.service.ResponseService;
import com.main.validator.AccountValidator;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(AccountController.API_ACCOUNT)
@Api(value = "account controller", description = "api account")
public class AccountController {
	//đặt url cho manageAccount
	public final static String API_ACCOUNT = "/api/v1/account";

	@Autowired
	AccountService accountService;

	//Để validate dữ liệu
	@Autowired
	AccountValidator accountValidator;

	//để lấy username, hay role,... từ token
	@Autowired
	JwtTokenService jwtTokenService;

	//name token default
	static final String DEFAULT_ACCESS_TOKEN_HEADER = "AccessToken";

	@Autowired
	private ResponseService responseService;

	//để đăng ký
	@Operation(summary = "to sign up for new users")
	@PostMapping(value = "/signup")
	public Object signUp(@Valid @RequestBody AccountPostDto accountPostDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = accountValidator.validateSignUpAccount(accountPostDto, result);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		accountService.insert(accountPostDto);
		return responseService.getResponseCustom("request.createSuccess", null, HttpStatus.CREATED);
	}

	//để create account
	@Operation(summary = "to create account by admin")
	@PostMapping(value = "/create")
	public Object create(@Valid @RequestBody AccountCreateDto accountCreateDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = accountValidator.validateCreateAccount(accountCreateDto, result);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		accountService.create(accountCreateDto);
		return responseService.getResponseCustom("request.createSuccess", null, HttpStatus.CREATED);
	}

	//để đăng nhập
	@Operation(summary = "to sign in")
	@PostMapping(value = "/signin")
	public Object signIn(@Valid @RequestBody AccountPostDto accountPostDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = accountValidator.validateSignInAccount(accountPostDto, result);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		Account account = accountService.findByUserNameAndReturnAccount(accountPostDto.getUserName());

		//tạo token
		String token = jwtTokenService.generateAccessToken(account);

		//Gắn value vào json qua Hashmap
		HashMap<String, String> map = new HashMap<>();
		map.put("token", token);
		map.put("typeAccount", account.getTypeAccount().toString());
		return responseService.getResponseCustom("request.signin.success", map, HttpStatus.OK);
	}

	//để lấy type account
	@Operation(summary = "get type account")
	@PostMapping(value = "/getTypeAccount")
	public Object getTypeAccount(@Valid @RequestBody AccountGetDto accountGetDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = accountValidator.validateGetTypeAccount(accountGetDto, result);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		Account account = accountService.findByUserNameAndReturnAccount(
				new AccountPostDto(accountGetDto.getUserName(), null,
						null).getUserName());
		//Gắn value vào json qua Hashmap
		HashMap<String, String> map = new HashMap<>();
		map.put("typeAccount", account.getTypeAccount().toString());
		return responseService.getResponseCustom("request.signin.success", map, HttpStatus.OK);
	}

	//để lấy danh sách account
	@Operation(summary = "get list account by admin")
	@GetMapping(value = "/list")
	public Object getTypeAccount() {
		return responseService.getResponseCustom("request.signin.success", accountService.list(), HttpStatus.OK);
	}
	
	//để lấy danh sách account có phân trang
	@Operation(summary = "get type account pagination by admin")
	@GetMapping(value = "/listPagination")
	public Object getTypeAccountPagination(
			@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "2") Integer pageSize
			){
		return responseService.getResponseCustom("request.signin.success",accountService.listPagination(pageNo,pageSize), HttpStatus.OK);
	}

	//để xóa account qua username
	@Operation(summary = "delete account by username")
	@DeleteMapping(value = "/delete")
	public Object deleteAccountById(@Param("username") String username,@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2) {
		List<ErrorMessage> listErrorResponses = accountValidator.validateDeleteAccount(username, authorizationV2.replace("Bearer ", ""));
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		accountService.deleteByIUserName(username);
		return responseService.getResponseCustom("request.deleteSuccess",null, HttpStatus.OK);
	}

	//update account qua username
	@Operation(summary = "update account by username (for admin)")
	@PutMapping(value = "/update")
	public Object updateAccount(@RequestBody AccountUpdateDto accountUpdateDto,@Param("username") String username,@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2) {
		List<ErrorMessage> listErrorResponses = accountValidator.validateUpdateAccount(
				username, authorizationV2.replace("Bearer ", ""));
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		return responseService.getResponseCustom("request.updateSuccess",accountService.updateAccountByUsername(username,accountUpdateDto), HttpStatus.OK);
	}

	//find all version 2 
	@Operation(summary = "find all account by pagination, you can find with query what you want")
	@GetMapping(value = "/findAll")
	public Object findAll(@QuerydslPredicate(root = Account.class) Predicate predicate,
			Pageable pageable) {
		return responseService.getResponseCustom("request.updateSuccess",accountService.findAll(predicate, pageable), HttpStatus.OK);
	}
}
