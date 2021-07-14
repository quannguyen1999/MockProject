package com.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.main.entity.ComboCourse;
import com.main.model.ErrorMessage;
import com.main.service.ComboCourseService;
import com.main.service.ResponseService;
import com.main.validator.ComboCourseValidator;
import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping(ComboCourseController.API_COMBO_COURSE)
public class ComboCourseController {
	//url chung cho category
	public final static String API_COMBO_COURSE = "/api/v1/comboCourse";

	//default token
	static final String DEFAULT_ACCESS_TOKEN_HEADER = "AccessToken";

	@Autowired
	private ComboCourseService comboCourseService;

	@Autowired
	private ComboCourseValidator comboCourseValidator;

	@Autowired
	private ResponseService responseService;

	//trả về list combo course qua id post type course
	@GetMapping(value = "/ListAllByIdPost")
	public Object getListByIdPostCourse(@RequestParam(defaultValue = "-1") String idPost) {
		List<ErrorMessage> listErrorResponses = comboCourseValidator.validateListComboByIdPostCourse(idPost);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		return responseService.getResponseCustom("request.getListSuccess", 	
				comboCourseService.listComboCoursesByIdPostCourse(idPost), HttpStatus.OK);
	}

	//trả về list course qua id course
	@GetMapping(value = "/findById")
	public Object findByid(@RequestParam(defaultValue = "-1") String idCourse) {
		List<ErrorMessage> listErrorResponses = comboCourseValidator.validateIdCourse(idCourse);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		return responseService.getResponseCustom("request.getListSuccess", 	
				comboCourseService.findById(idCourse), HttpStatus.OK);
	}

	//find all version 2 
	@GetMapping(value = "/findAll")
	public Object findAll(@QuerydslPredicate(root = ComboCourse.class) Predicate predicate,
			Pageable pageable) {
		return responseService.getResponseCustom("request.updateSuccess",comboCourseService.findAll(predicate, pageable), HttpStatus.OK);
	}

	//	//Update registration
	//	@PutMapping(value = "/update")
	//	public Object updateRegistration(@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2, 	
	//			@RequestParam String id,
	//			@RequestBody CourseComboPutDto courseComboPutDto,BindingResult result) {
	//		List<ErrorMessage> listErrorResponses = comboCourseValidator.validateUpdateComboPost(courseComboPutDto, result);
	//		if(listErrorResponses.size() > 0) {
	//			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
	//		}
	//		return responseService.getResponseCustom("request.updateSuccess", registrationService.updateRegistration(registrationPutDto,Integer.parseInt(id)), HttpStatus.OK);
	//	}

















}
