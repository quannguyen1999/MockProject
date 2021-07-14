package com.main.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.main.entity.Course;
import com.main.model.CoursePostDto;
import com.main.model.CoursePutDto;
import com.main.model.CustomResponse;
import com.main.model.ErrorMessage;
import com.main.model.RegistrationPostDto;
import com.main.service.CourseService;
import com.main.service.JwtTokenService;
import com.main.service.RegistrationService;
import com.main.service.ResponseService;
import com.main.validator.CategoryValidator;
import com.main.validator.CourseValidator;
import com.main.validator.PostValidator;
import com.main.validator.RegistrationValidator;
import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping(CourseController.API_COURSE)
public class CourseController {
	//link common for api course
	public final static String API_COURSE = "/api/v1/course";

	//default access token
	static final String DEFAULT_ACCESS_TOKEN_HEADER = "AccessToken";

	//to validate course
	@Autowired
	private CourseValidator courseValidator;

	//to get message from message_url.properties
	@Autowired
	private MessageSource messageSource;

	//to crud
	@Autowired
	private CourseService courseService;

	//to get username, typeAccount,... from token
	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private CategoryValidator categoryValidator;

	@Autowired
	private PostValidator postValidator;

	@Autowired
	private RegistrationValidator registrationValidator;

	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private ResponseService responseService;

	//to create course
	@PostMapping(value = "/create")
	public Object createCategory(@RequestBody CoursePostDto coursePostDto,
			BindingResult result,
			@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2) {
		List<ErrorMessage> listErrorResponses = courseValidator.validateCreateCourse(coursePostDto, result);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		};
		coursePostDto.setUserName(jwtTokenService.getUserName(authorizationV2.replace("Bearer ", "")));
		courseService.createCourse(coursePostDto);
		return new ResponseEntity<>(new CustomResponse(null,
				messageSource.getMessage("request.createSuccess",null, Locale.ENGLISH),null)
				,HttpStatus.CREATED);
	}

	//for client, list course by idCategory
	@GetMapping(value = "/listByIdCategory")
	public Object getAllListCourseByIdCategory(
			@Param("id") String id
			){
		List<ErrorMessage> listErrorResponses = categoryValidator.validateIdCategory(id);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		};
		return new ResponseEntity<>(new CustomResponse(
				null,
				messageSource.getMessage("request.getListSuccess",null, Locale.ENGLISH),
				courseService.listAllCourseByIdCategory(Integer.parseInt(id))),
				HttpStatus.OK);
	}

	//for admin
	@GetMapping(value = "/listPagination")
	public Object getListCourse(
			@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "2") Integer pageSize
			){
		return new ResponseEntity<>(new CustomResponse(
				null,
				messageSource.getMessage("request.getListSuccess",null, Locale.ENGLISH),
				courseService.listPagination(pageNo,pageSize)),
				HttpStatus.OK);
	}

	//for client
	@GetMapping(value = "/listCourseByIdPost")
	public Object getListCourseByIdCategory(
			@RequestParam(defaultValue = "id") String id,
			@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "2") Integer pageSize
			){
		List<ErrorMessage> listErrorResponses = postValidator.validateIdPost(id);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		return new ResponseEntity<>(new CustomResponse(
				null,
				messageSource.getMessage("request.getListSuccess",null, Locale.ENGLISH),
				courseService.listAllCourseByIdPostMenu(Integer.parseInt(id))),
				HttpStatus.OK);
	}

	//delete course by id
	@DeleteMapping(value = "/delete")
	public Object createCategory(@Param("id") String id,
			@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2) {
		List<ErrorMessage> listErrorResponses = courseValidator.validateIdCourse(id);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		courseService.deleteCourseById(Integer.parseInt(id));
		return new ResponseEntity<>(new CustomResponse(null,
				messageSource.getMessage("request.deleteSuccess",null, Locale.ENGLISH),null)
				,HttpStatus.OK);
	}

	//update course by id
	@PutMapping(value = "/update")
	public Object createCategory(@Param("id") String id,
			@RequestBody CoursePutDto coursePutDto) {
		List<ErrorMessage> listErrorResponses = courseValidator.validateUpdateCourse(id, coursePutDto);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		return new ResponseEntity<>(new CustomResponse(null,
				messageSource.getMessage("request.updateSuccess",null, Locale.ENGLISH),
				courseService.updateCourse(coursePutDto, Integer.parseInt(id))
				)
				,HttpStatus.OK);
	}


	//find course by id
	@GetMapping(value = "/find")
	public Object findCourseById(@Param("id") String id) {
		List<ErrorMessage> listErrorResponses = courseValidator.validateIdCourse(id);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		;
		return new ResponseEntity<>(new CustomResponse(null,
				messageSource.getMessage("request.deleteSuccess",null, Locale.ENGLISH),courseService.findCourseById(Integer.parseInt(id)))
				,HttpStatus.OK);
	}

	//register course
	@PostMapping(value = "/register")
	public Object registerCourse(@RequestBody @Valid RegistrationPostDto registrationPostDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = registrationValidator.validateRegisterCourse(registrationPostDto,result);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		};
		return new ResponseEntity<>(new CustomResponse(null,
				messageSource.getMessage("request.createSuccess",null, Locale.ENGLISH),
				registrationService.register(registrationPostDto))
				,HttpStatus.OK);
	}

	//find all version 2 
	@GetMapping(value = "/findAll")
	public Object findAll(@QuerydslPredicate(root = Course.class) Predicate predicate,
			Pageable pageable) {
		return new ResponseEntity<>(new CustomResponse(
				null,
				messageSource.getMessage("request.getListSuccess",null, Locale.ENGLISH),
				courseService.findAll(predicate, pageable)),
				HttpStatus.OK); 
	}

}
