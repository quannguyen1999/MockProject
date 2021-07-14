package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.model.CategoryGetDto;
import com.example.demo.model.CustomResponse;
import com.example.demo.service.CategoryService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	//source page html
	private static final String PAGE_HOME = "home";
	private static final String PAGE_LOGIN = "login";
	private static final String PAGE_WELCOME = "welcome";
	
	@Autowired
	private CategoryService categoryService;
	
	//========================>return default home page for client
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		//return list category menu for navbar
		CustomResponse customResponse = categoryService.getListCategoryHomePage();	
		
		//then convert object to List<CategoryGetDto>
		List<CategoryGetDto> categoryPostDtos = null;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		categoryPostDtos= objectMapper.convertValue(customResponse.getData(),objectMapper.getTypeFactory().constructCollectionLikeType(List.class, CategoryGetDto.class));

		//and add attribute
		model.addAttribute("listCategory",categoryPostDtos);
		
		return PAGE_HOME;
	}

	/*
	 * ==============================>Return page login
	 * principal: to check use has role or not
	 * 
	 * */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Principal principal,
			HttpServletRequest request,
			Model model,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		if(session != null && session.getAttribute("error") != null) {
			model.addAttribute("messageError", session.getAttribute("error"));
			session.invalidate();
		}
		//if use had login then return page welcome
		return principal!=null ? "redirect:/welcome": PAGE_LOGIN;
	}
	
	//return page welcome for user had login
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String welcome(HttpServletRequest request) {
		//get session from message
		HttpSession session = request.getSession();
		String message = (String) session.getAttribute("messageSuccess");
		
		//if message exists then set message to request
		if(message != null && message.isEmpty() == false) {
			request.setAttribute("messageSuccess", message);
			session.setAttribute("messageSuccess", null);
		}
		
		return PAGE_WELCOME;
	}
	
	
}
