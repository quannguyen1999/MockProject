package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
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
import com.example.demo.model.CourseGetDto;
import com.example.demo.model.CustomResponse;
import com.example.demo.model.PostPostDto;
import com.example.demo.model.RegistrationGetDto;
import com.example.demo.model.RegistrationPostDto;
import com.example.demo.service.ConvertService;
import com.example.demo.service.CourseService;
import com.example.demo.service.PostService;
import com.example.demo.service.RegistrationService;

@Controller
@RequestMapping(value = "/registration")
public class RegistrationController {

	//source page html
	private static final String PAGE_REGISTER_SUCCESS = "manageCourse/registrationCourseSuccess";
	private static final String PAGE_LIST = "manageRegistration/listRegistration";
	private static final String PAGE_LIST_CUSTOMER_BUY_COURSE = "manageRegistration/listCustomerBuyCourse";

	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private ConvertService convertService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private PostService postService;
	
	/*===================================>process save registration
	 * redirAttrs: to set flash attribute
	 * request: to get session
	 * postPostDto: to get value
	 */
	@RequestMapping(value = "/saveRegistration", method = RequestMethod.POST,produces = "text/html; charset=UTF-8")
	public String listPagination(
			@RequestParam(value = "id") Optional<Integer> id,
			@RequestParam(value = "idComboCourse") Optional<String> idComboCourse,
			RedirectAttributes redirAttrs,
			HttpServletRequest request,
			@ModelAttribute("registration") RegistrationPostDto registrationPostDto
			) {
		System.out.println(registrationPostDto);
		Integer idCourse = id.orElse(-1);
		String idCombo = idComboCourse.orElse("none");
		if(idCourse == -1 && idCombo.equalsIgnoreCase("none") ||
				idCourse != -1 && idCombo.equalsIgnoreCase("none") == false
				) {
			return "redirect:/error";
		}
		if(registrationPostDto.getListComboCourse() == null) {
			registrationPostDto.setListComboCourse(new ArrayList<Integer>());
		}
		if(idCombo.equalsIgnoreCase("none") == false) {
			String[] idListCourse = idCombo.split("-");
			for(int i=0;i<idListCourse.length;i++) {
				registrationPostDto.getListComboCourse().add(Integer.parseInt(idListCourse[i]));
			}
		}else {
			registrationPostDto.getListComboCourse().add(idCourse);
		}
		CustomResponse customResponse = registrationService.register(registrationPostDto);
		System.out.println(customResponse);
		if(customResponse.getListErrorMessages() != null) {
			redirAttrs.addFlashAttribute("customResponse",customResponse);
			redirAttrs.addFlashAttribute("registration",registrationPostDto);
			return "redirect:/course/detail?id="+id;
		}
		return PAGE_REGISTER_SUCCESS;
	}
	
	

	/*===================================>when use request with get, it will auto redirect to page before
	 * redirAttrs: to set flash attribute
	 * request: to get session
	 * postPostDto: to get value
	 */
	@RequestMapping(value = "/saveRegistration", method = RequestMethod.GET,produces = "text/html; charset=UTF-8")
	public String returnPageCourse(
			@RequestParam(value = "id") Integer id,
			RedirectAttributes redirAttrs,
			HttpServletRequest request
			) {
		return "redirect:/course/detail?id="+id;
	}
	
	/*
	 * ==============================>return list registration for admin
	 * model: to add attribute
	 * request: to get session
	 * page:to get current page choose
	 * 
	 * */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String listPagination(
			Model model, 
			HttpServletRequest request,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("urlRequest") Optional<String> urlRequest
			) {
		CustomResponse customResponse;
		Page<RegistrationGetDto> listPageImpl = null;
		
		//get current page and pageSize default
		int currentPage = page.orElse(0);
		int pageSize = 5;
	
		//call session to get token
		HttpSession session =request.getSession();
		String token = (String) session.getAttribute("token");

		//list account by current page and page size
		String getUrlRequest = urlRequest.orElse(null);
		if(getUrlRequest != null) {
			getUrlRequest = getUrlRequest.replace("$", "&");
			
			customResponse = registrationService.findAll(token, "?page="+
					currentPage+"&size="+pageSize+"&"+getUrlRequest);
			
			model.addAttribute("getUrlRequest", getUrlRequest);
			
			listPageImpl = convertService.convertToResponsePage(customResponse);
		}else {
			//list account by current page and page size
			customResponse = registrationService.getListRegistration(token,currentPage, pageSize);
			listPageImpl = convertService.convertToResponsePage(customResponse);
		
		}
		
		System.out.println(listPageImpl.getContent());
		
		//add attribute to page
		model.addAttribute("registrationPage", listPageImpl);
		
		//add page choose current
		model.addAttribute("pageChoose", currentPage);
		
		return PAGE_LIST;
	}
	
	/*
	 * ==============================>return list customer had buy course (find by id course) --  for client
	 * model: to add attribute
	 * request: to get session
	 * page:to get current page choose
	 * 
	 * */
	@RequestMapping(value = "/listCustomerBuyCourse", method = RequestMethod.GET)
	public String listCustomerBuyCourse(
			Model model, 
			HttpServletRequest request,
			@RequestParam("id") Integer id
			) {
		//get detail course
		CustomResponse customResponse = courseService.findCourseByid(id);
		CourseGetDto courseGetDto = convertService.convertToObject(customResponse, new CourseGetDto());
		model.addAttribute("course", courseGetDto);
		
		//otherwise return page default
		CustomResponse cusResponsePostDetail = postService.getDetailPost(courseGetDto.getIdPost());
		PostPostDto postPostDto = convertService.convertToObject(cusResponsePostDetail, new PostPostDto());
		
		CustomResponse cusResponse = postService.listPostMenuByIdCategory(postPostDto.getIdCategory());
		List<PostPostDto> listPostDtos =  convertService.convertToList(cusResponse, new PostPostDto());

		//to list menu for page
		model.addAttribute("listSubCategory",listPostDtos);
		
		CustomResponse customResponseListCustomer = registrationService.getListRegistrationByidCourse(id);
		if(customResponseListCustomer.getListErrorMessages() != null) {
			
		}else {
			List<RegistrationGetDto> listRegistrationGetDtos = convertService.convertToList(customResponseListCustomer, new RegistrationGetDto());
			model.addAttribute("listRegistration", listRegistrationGetDtos);
		}
		
		return PAGE_LIST_CUSTOMER_BUY_COURSE;
	}
}
