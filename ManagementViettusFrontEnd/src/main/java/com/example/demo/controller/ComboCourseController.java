package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.CategoryPostDto;
import com.example.demo.model.ContactGetDto;
import com.example.demo.model.CourseGetDto;
import com.example.demo.model.CustomResponse;
import com.example.demo.model.PostPostDto;
import com.example.demo.model.RegistrationPostDto;
import com.example.demo.model.TypeCategory;
import com.example.demo.model.TypePay;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ComboCourseService;
import com.example.demo.service.ConvertService;
import com.example.demo.service.CourseService;
import com.example.demo.service.PostService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/comboCourse")
public class ComboCourseController {
	//file source html
	private static final String PAGE_CREATE_COMBO = "manageComboCourse/createCombo";
	private static final String PAGE_VERIFY_COMBO = "manageComboCourse/verifyCombo";

	@Autowired
	private CourseService courseService;

	@Autowired
	private ConvertService convertService;

	@Autowired
	private PostService postService;
	
	@Autowired
	private ComboCourseService comboCourseService;
	
	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/createCombo", method = RequestMethod.GET)
	public String getDetailCategoryById(
			Model model, 
			HttpServletRequest request,
			@RequestParam("id") String id
			) {
		//page return default
		String pageReturn = PAGE_CREATE_COMBO;

		//find category by id
		CustomResponse customResponse = categoryService.getCategoryById(id);

		//if category no exists then send message error
		if(customResponse.getListErrorMessages() != null) {
			//send message error
			request.setAttribute("meesageError", customResponse.getListErrorMessages().get(0).getMessage());
		}else {
			//convert Object to categoryPostDto
			CategoryPostDto categoryPostDto = null;
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			categoryPostDto = objectMapper.convertValue(customResponse.getData(), CategoryPostDto.class);

			//find all post by id category base on current page and pagesize
			PageImpl<PostPostDto> listPageImpl = null;
			model.addAttribute("category", categoryPostDto);
			
			if(categoryPostDto.getTypeCategory() != null && categoryPostDto.getTypeCategory().toString().equalsIgnoreCase(TypeCategory.COURSE.toString())){
				//otherwise return page default
				CustomResponse cusResponse = postService.listPostMenuByIdCategory(Integer.parseInt(id));
				List<PostPostDto> listPostDtos =  convertService.convertToList(cusResponse, new PostPostDto());

				//to list menu for page
				model.addAttribute("listSubCategory",listPostDtos);

				CustomResponse cusResponseListCourse = courseService.getListCourseByIdCategory(id);
				if(cusResponseListCourse.getListErrorMessages() != null) {

				}else {
					model.addAttribute("listCourse", convertService.convertToList(cusResponseListCourse, new CourseGetDto()));
				}

			}else {
				//otherwise return page default
				CustomResponse cusResponse = postService.listPostMenuByIdCategory(Integer.parseInt(id));
				List<PostPostDto> listPostDtos =  convertService.convertToList(cusResponse, new PostPostDto());

				//to list menu for page
				model.addAttribute("listSubCategory",listPostDtos);
			}
		}
		return pageReturn;
	}
	
	@RequestMapping(value = "/verifyCombo", method = RequestMethod.GET)
	public String verifyCombo(
			Model model, 
			HttpServletRequest request,
			@RequestParam("id") String id,
			@RequestParam("idCategory") String idCategory
			) {
		List<CourseGetDto> listCourseGetDtos = new ArrayList<CourseGetDto>();
		String[] listidCourse =  id.split("-");
		for(int i=0;i<listidCourse.length;i++) {
			CustomResponse customResponse = courseService.findCourseByid(Integer.parseInt(listidCourse[i]));
			if(customResponse.getListErrorMessages() == null) {
				listCourseGetDtos.add(convertService.convertToObject(customResponse, new CourseGetDto()));
			}
		}
		//page return default
		String pageReturn = PAGE_VERIFY_COMBO;
		
		model.addAttribute("idCategory", idCategory);
		
		model.addAttribute("idComboCourse", id);
		
		if(listCourseGetDtos == null || listCourseGetDtos.size() <= 0) {
			return "redirect:/comboCourse/createCombo?id="+idCategory;
		}
		
		RegistrationPostDto registrationPostDto = new RegistrationPostDto();
		registrationPostDto.setTypePay(TypePay.BANK);
		model.addAttribute("registration", registrationPostDto);
		
		model.addAttribute("listCourse", listCourseGetDtos);
		
		return pageReturn;
	}
	
	
	
}