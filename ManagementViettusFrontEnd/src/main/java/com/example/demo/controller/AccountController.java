package com.example.demo.controller;

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

import com.example.demo.model.AccountGetDto;
import com.example.demo.model.CustomResponse;
import com.example.demo.service.AccountService;
import com.example.demo.service.ConvertService;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/account")
public class AccountController {
	//source page html
	private static final String PAGE_LIST_ACCOUNT = "manageAccount/listAccount";

	//provide crud
	@Autowired
	private AccountService accountService;

	@Autowired
	private ConvertService convertService;

	/*
	 * ==============================>return list account for admin
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
		Page<AccountGetDto> listPageImpl = null;

		//call session to get token
		HttpSession session =request.getSession();
		String token = (String) session.getAttribute("token");

		//get current page and pageSize default
		int currentPage = page.orElse(0);
		int pageSize = 5;

		String getUrlRequest = urlRequest.orElse(null);
		if(getUrlRequest != null) {
			getUrlRequest = getUrlRequest.replace("$", "&");
			
			customResponse = accountService.findAll(token, "?page="+
					currentPage+"&size="+pageSize+"&"+getUrlRequest);
			
			model.addAttribute("getUrlRequest", getUrlRequest);
			
			listPageImpl = convertService.convertToResponsePage(customResponse);
		}else {
			//list account by current page and page size
			customResponse = accountService.getListAccount(token,currentPage, pageSize);
			listPageImpl = convertService.convertToResponsePage(customResponse);
		}

		//add attribute to page
		model.addAttribute("accountPage", listPageImpl);

		//add page choose current
		model.addAttribute("pageChoose", currentPage);

		return PAGE_LIST_ACCOUNT;
	}
}
