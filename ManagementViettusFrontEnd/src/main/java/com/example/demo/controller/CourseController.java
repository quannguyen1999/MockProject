package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.AccountGetDto;
import com.example.demo.model.CategoryPostDto;
import com.example.demo.model.ComboCourseGetDto;
import com.example.demo.model.CourseGetDto;
import com.example.demo.model.CoursePostDto;
import com.example.demo.model.CoursePutDto;
import com.example.demo.model.CustomResponse;
import com.example.demo.model.PostPostDto;
import com.example.demo.model.RegistrationPostDto;
import com.example.demo.model.TypePay;
import com.example.demo.service.ComboCourseService;
import com.example.demo.service.ConvertService;
import com.example.demo.service.CourseService;
import com.example.demo.service.PostService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/course")
public class CourseController {
	//file source html
	private static final String PAGE_LIST_COURSE = "manageCourse/listCourse";
	private static final String PAGE_CREATE_COURSE = "manageCourse/createCourse";
	private static final String PAGE_UPDATE_COURSE = "manageCourse/updateCourse";
	private static final String PAGE_DETAIL_COURSE = "manageCourse/detailCourse";

	@Autowired
	private CourseService courseService;

	@Autowired
	private ConvertService convertService;

	@Autowired
	private PostService postService;
	
	@Autowired
	private ComboCourseService comboCourseService;

	/*
	 * ======================>get list course for admin
	 * model: add attribute
	 * request: set attribute
	 * page: get current page choose
	 * */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String listPagination(
			Model model, 
			HttpServletRequest request,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("urlRequest") Optional<String> urlRequest
			) {
		CustomResponse customResponse;
		Page<CourseGetDto> listPageImpl = null;
		
		//check if had message before request from function addFlashAttribute
		if(model.containsAttribute("messageSuccess") == true)
			request.setAttribute("messageSuccess", request.getAttribute("messageSuccess"));
		if(model.containsAttribute("meesageError") == true)
			request.setAttribute("meesageError", request.getAttribute("meesageError"));

		//to get current page and pageSize default
		int currentPage = page.orElse(0);
		int pageSize = 5;

		//to get token from session
		HttpSession session =request.getSession();
		String token = (String) session.getAttribute("token");

		//list on post base on current page and page size
		String getUrlRequest = urlRequest.orElse(null);
		if(getUrlRequest != null) {
			getUrlRequest = getUrlRequest.replace("$", "&");
			
			customResponse = courseService.findAll(token, "?page="+
					currentPage+"&size="+pageSize+"&"+getUrlRequest);
			
			model.addAttribute("getUrlRequest", getUrlRequest);
			
			listPageImpl = convertService.convertToResponsePage(customResponse);
		}else {
			//list account by current page and page size
			customResponse = courseService.getListCourse(token,currentPage, pageSize);
			listPageImpl = convertService.convertToResponsePage(customResponse);
		
		}

		model.addAttribute("postPage", listPageImpl);
		model.addAttribute("pageChoose", currentPage);

		return PAGE_LIST_COURSE;
	}

	/*
	 * ======================>get detail course for client
	 * model: add attribute
	 * request: set attribute
	 * page: get current page choose
	 * */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String listPagination(
			Model model, 
			HttpServletRequest request,
			@RequestParam("id") Integer id
			) {
		//otherwise return page default
		CustomResponse cusResponse = courseService.findCourseByid(id);
		if(cusResponse.getListErrorMessages() != null) {
			return "redirect:/error";
		}else {
			CourseGetDto courseGetDto =  convertService.convertToObject(cusResponse, new CourseGetDto());

			CustomResponse customResponsePostDto = postService.getDetailPost(courseGetDto.getIdPost());
			
			PostPostDto postPostDto = convertService.convertToObject(customResponsePostDto, new PostPostDto());

			//otherwise return page default
			CustomResponse cusResponseListPost = postService.listPostMenuByIdCategory(postPostDto.getIdCategory());
			
			List<PostPostDto> listPostDtos =  convertService.convertToList(cusResponseListPost, new PostPostDto());

			//to list menu for page
			model.addAttribute("listSubCategory",listPostDtos);
			
			//to list menu for page
			
			model.addAttribute("idCategory", postPostDto.getIdCategory());
			model.addAttribute("idPost", courseGetDto.getIdPost());
			model.addAttribute("idCourse", courseGetDto.getIdCourse());
			
			model.addAttribute("nameCategory", postPostDto.getNameCategory());
			model.addAttribute("namePost", courseGetDto.getNamePost());
			model.addAttribute("nameCourse", courseGetDto.getTitle());
			
			model.addAttribute("course", courseGetDto);
			
			CustomResponse cusResponseListCourse = courseService.getListCourseByidPostWithStatusIsTrue(postPostDto.getIdPost());
			if(cusResponseListCourse.getListErrorMessages() != null) {
				
			}else {
				model.addAttribute("listCourse", convertService.convertToList(cusResponseListCourse, new CourseGetDto()));
			}
			
			CustomResponse customResponseComboCourse = comboCourseService.listComboCoursesByIdPostCourse(courseGetDto.getIdPost());
			model.addAttribute("listComboCourse", customResponseComboCourse.getListErrorMessages() == null ? convertService.convertToList(customResponseComboCourse, new ComboCourseGetDto()) : null);
			
			//check if had message before request from function addFlashAttribute
			if(model.containsAttribute("customResponse") == true) {
				request.setAttribute("customResponse",(CustomResponse) request.getAttribute("customResponse"));
				model.addAttribute("registration",  (RegistrationPostDto) request.getAttribute("registration"));
			}else {
				RegistrationPostDto registrationPostDto = new RegistrationPostDto();
				List<Integer> listIntegers = Arrays.asList(courseGetDto.getIdCourse());
				registrationPostDto.setListComboCourse(listIntegers);
				registrationPostDto.setTypePay(TypePay.BANK);
				model.addAttribute("registration", registrationPostDto);
			}
		}

		return PAGE_DETAIL_COURSE;
	}

	/*==============================>to create new course
	 * model: add attribute
	 * request: set utf 
	 * response: set utf
	 * */
	@RequestMapping(value = "/create", method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
	public String listPagination(
			Model model, 
			HttpServletRequest request,
			HttpServletResponse response
			) throws UnsupportedEncodingException {
		//to get token from session
		HttpSession session =request.getSession();
		String token = (String) session.getAttribute("token");

		//set utf 8
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		CustomResponse cusResponsePostCourse = postService.getListPostCourse(token);
		//list all post course
		if(cusResponsePostCourse.getListErrorMessages() != null) {
			request.setAttribute("meesageError", cusResponsePostCourse.getListErrorMessages().get(0).getMessage());
		}else {
			List<PostPostDto> listPostCourse = convertService.convertToList(cusResponsePostCourse, new PostPostDto());
			model.addAttribute("listPostCourse", listPostCourse);
		}

		//init COURSE
		model.addAttribute("course", new CoursePostDto());
		return PAGE_CREATE_COURSE;
	}



	/*===================================>process save course
	 * redirAttrs: to set flash attribute
	 * request: to get session
	 * postPostDto: to get value
	 */
	@RequestMapping(value = "/saveCourse", method = RequestMethod.POST,produces = "text/html; charset=UTF-8")
	public String listPagination(
			RedirectAttributes redirAttrs,
			HttpServletRequest request,
			@ModelAttribute("course") CoursePostDto coursePostDto
			) {
		//get session
		HttpSession session = request.getSession();

		//create post base on token
		CustomResponse customResponse = courseService.createCourse(coursePostDto,(String)session.getAttribute("token"));

		if(customResponse.getListErrorMessages() != null) {
			redirAttrs.addFlashAttribute("meesageError", customResponse.getListErrorMessages().get(0).getMessage().toString());
		}else {
			redirAttrs.addFlashAttribute("messageSuccess", customResponse.getMessage());
		}

		return "redirect:/course/create";
	}

	/*===================================>process save course
	 * redirAttrs: to set flash attribute
	 * request: to get session
	 * postPostDto: to get value
	 */
	@RequestMapping(value = "/updateCourse", method = RequestMethod.POST,produces = "text/html; charset=UTF-8")
	public String updateCourse(
			RedirectAttributes redirAttrs,
			HttpServletRequest request,
			@ModelAttribute("course") CoursePostDto coursePostDto,
			@RequestParam("id") Integer idCourse
			) {
		//get session
		HttpSession session = request.getSession();

		CoursePutDto coursePutDto = new CoursePutDto(coursePostDto.getTitle(),
				coursePostDto.getContent(),
				coursePostDto.getImage(),
				coursePostDto.getTypeLearn(), 
				null, coursePostDto.getLinkCourse(), coursePostDto.getIdPost());

		//create post base on token
		CustomResponse customResponse = courseService.updateCourse(coursePutDto,(String)session.getAttribute("token"),idCourse);

		if(customResponse.getListErrorMessages() != null) {
			System.out.println(customResponse);
			redirAttrs.addFlashAttribute("meesageError", customResponse.getListErrorMessages().get(0).getMessage().toString());
			return "redirect:/course/find?id="+idCourse;
		}else {
			redirAttrs.addFlashAttribute("messageSuccess", customResponse.getMessage());
		}

		return "redirect:/course/list";
	}


	/*===================================>process find course by id
	 * redirAttrs: to set flash attribute
	 * request: to get session
	 * postPostDto: to get value
	 */
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public String findCourseById(
			RedirectAttributes redirAttrs,
			HttpServletRequest request,
			Model model,
			@RequestParam("id") Integer idCourse
			) {
		//get session
		HttpSession session = request.getSession();

		String token = (String) session.getAttribute("token");

		//create post base on token
		CustomResponse customResponse = courseService.findCourseByid(idCourse);

		if(customResponse.getListErrorMessages() != null) {
			redirAttrs.addFlashAttribute("meesageError", customResponse.getListErrorMessages().get(0).getMessage().toString());
		}else {
			model.addAttribute("idCourse", idCourse);

			redirAttrs.addFlashAttribute("messageSuccess", customResponse.getMessage());
			model.addAttribute("course",convertService.convertToObject(customResponse, new CourseGetDto()));

			CustomResponse cusResponsePostCourse = postService.getListPostCourse(token);
			List<PostPostDto> listPostCourse = convertService.convertToList(cusResponsePostCourse, new PostPostDto());
			model.addAttribute("listPostCourse", listPostCourse);
		}

		return PAGE_UPDATE_COURSE;
	}
}
