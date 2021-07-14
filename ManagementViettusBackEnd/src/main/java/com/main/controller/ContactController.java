package com.main.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.model.ContactPostDto;
import com.main.model.ErrorMessage;
import com.main.service.ContactService;
import com.main.service.ResponseService;
import com.main.validator.ContactValidator;
@RestController
@RequestMapping(ContactController.API_CONTACT)
public class ContactController {
	public final static String API_CONTACT = "/api/v1/contact";

	static final String DEFAULT_ACCESS_TOKEN_HEADER = "AccessToken";

	@Autowired
	private ContactService contactService;

	@Autowired
	private ContactValidator contactValidator;

	@Autowired
	private ResponseService responseService;

	@PostMapping(value = "/create")
	public Object createCategory(@Valid @RequestBody ContactPostDto contactPostDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = contactValidator.validateContactPost(contactPostDto, result);
		if (listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		contactService.saveContact(contactPostDto);
		return responseService.getResponseCustom("request.createSuccess", null, HttpStatus.CREATED);
	}

	@GetMapping(value = "/list")
	public Object getList(@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2) {
		return responseService.getResponseCustom("request.getListSuccess", contactService.findAll(), HttpStatus.OK);
	
	}

	@GetMapping(value = "/listPagination")
	public Object getListByEmail(@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "9") Integer pageSize) {
		return responseService.getResponseCustom("request.getListSuccess", 	contactService.listPagination(pageNo, pageSize), HttpStatus.OK);
	
	}

	@GetMapping(value = "/listEmail")
	public Object getListByEmail(@Param("email") String email,
			@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "9") Integer pageSize) {
		return responseService.getResponseCustom("request.getListSuccess", 	contactService.listPaginationWithEmail(pageNo, pageSize, email), HttpStatus.OK);
	
	}

	@GetMapping(value = "/listPhone")
	public Object getListByPhone(@Param("phone") String phone,
			@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "9") Integer pageSize) {
		return responseService.getResponseCustom("request.getListSuccess", contactService.listPaginationWithPhone(pageNo, pageSize, phone), HttpStatus.OK);
	
	}

	@DeleteMapping(value = "/delete")
	public Object createCategory(@Param("id") String id,
			@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2) {
		List<ErrorMessage> listErrorResponses = contactValidator.validateIdContact(id);
		if (listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		contactService.deleteContactById(Integer.parseInt(id));
		return responseService.getResponseCustom("request.deleteSuccess", null, HttpStatus.OK);
	}
}
