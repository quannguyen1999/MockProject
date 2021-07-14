package com.example.demo.controller;

import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//to control error
@Controller
public class ErrorHandlerController{
	//source page html
	public static final String PAGE_NOT_FOUND = "error/pageError";
	private static final String PAGE_ACCESS_DENIED = "error/pageAccessDenied";
	private static final String PAGE_INTERNAL_SERVER_ERROR = "error/internalServerError";

	@RequestMapping(value = "error", method = RequestMethod.GET)
	public String getErrorNotFound(Model model, HttpServletResponse response) {
		if(response.getStatus() == 500) {
			return PAGE_INTERNAL_SERVER_ERROR;
		}
		model.addAttribute("messages", "Trang không tìm thấy");
		return PAGE_NOT_FOUND;
	}

	@RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
	public String accessDenied(Model model) {
		model.addAttribute("messages", "Bạn không đủ quyền");
		return PAGE_ACCESS_DENIED;
	}
}
