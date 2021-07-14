package com.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.entity.Registration;
import com.main.model.ErrorMessage;
import com.main.model.RegistrationPutDto;
import com.main.service.RegistrationService;
import com.main.service.ResponseService;
import com.main.validator.CourseValidator;
import com.main.validator.RegistrationValidator;
import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping(RegistrationController.API_REGISTRATION)
public class RegistrationController {
	//đường link chung api cho danh sach dang ky
	public final static String API_REGISTRATION = "/api/v1/registration";

	//default token
	static final String DEFAULT_ACCESS_TOKEN_HEADER = "AccessToken";

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private RegistrationValidator registrationValidator;

	@Autowired
	private CourseValidator courseValidator;

	@Autowired
	private ResponseService responseService;

	//trả về list registration
	@GetMapping(value = "/list")
	public Object getListRegistration(@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2, 	
			@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "2") Integer pageSize) {
		return responseService.getResponseCustom("request.getListSuccess", registrationService.listPagination(pageNo, pageSize), HttpStatus.OK);
	}

	//Update registration
	@PutMapping(value = "/update")
	public Object updateRegistration(@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2, 	
			@RequestParam String id,
			@RequestBody RegistrationPutDto registrationPutDto) {
		List<ErrorMessage> listErrorResponses = registrationValidator.validateUpdateRegistration(registrationPutDto, id);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		return responseService.getResponseCustom("request.updateSuccess", registrationService.updateRegistration(registrationPutDto,Integer.parseInt(id)), HttpStatus.OK);
	}

	//list all registration by id course
	@GetMapping(value = "/listByIdCourse")
	public Object updateRegistration( 	
			@RequestParam String idCourse) {
		List<ErrorMessage> listErrorResponses = courseValidator.validateIdCourse(idCourse);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		return responseService.getResponseCustom("request.getListSuccess", 	registrationService.listAllRegistrationByIdCourseAndStatusIsTrue(Integer.parseInt(idCourse)), HttpStatus.OK);
	}

	//find All 
	@GetMapping(value = "/findAll")
	public Object findAll(
			@QuerydslPredicate(root = Registration.class) Predicate predicate,
			Pageable pageable) {
		return responseService.getResponseCustom("request.getListSuccess", registrationService.findAll(predicate, pageable), HttpStatus.OK);
	}

}
