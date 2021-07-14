package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
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

import com.example.demo.model.ContactGetDto;
import com.example.demo.model.ContactPostDto;
import com.example.demo.model.CoursePostDto;
import com.example.demo.model.CustomResponse;
import com.example.demo.model.PostPostDto;
import com.example.demo.service.ContactService;
import com.example.demo.service.ConvertService;
import com.example.demo.service.CourseService;
import com.example.demo.service.PostService;

@Controller
@RequestMapping(value = "/contact")
public class ContactController {
	private static final String PAGE_LIST_CONTACT = "manageContact/listContact";
	private static final String PAGE_CREATE_CONTACT = "manageContact/contact";

	@Autowired
	private ContactService contactService;

	@Autowired
	private ConvertService convertService;

	/*
	 * ======================>get list course for admin model: add attribute
	 * request: set attribute page: get current page choose
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String listPagination(Model model, HttpServletRequest request,
			@RequestParam("page") Optional<Integer> page) {
		// to get current page and pageSize default
		int currentPage = page.orElse(0);
		int pageSize = 5;

		// to get token from session
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");

		// list on post base on current page and page size
		CustomResponse customResponse = contactService.getListContact(token, currentPage, pageSize);
		Page<ContactPostDto> listPageImpl = convertService.convertToResponsePage(customResponse);

		model.addAttribute("postPage", listPageImpl);
		model.addAttribute("pageChoose", currentPage);

		return PAGE_LIST_CONTACT;
	}

	/*
	 * ======================>get list contact by email for admin request: set
	 * attribute page: get current page choose
	 */
	@RequestMapping(value = "/listByEmail")
	public String listByEmailPagination(@RequestParam("email") String email, Model model, HttpServletRequest request,
			@RequestParam("page") Optional<Integer> page) {
		// to get current page and pageSize default
		int currentPage = page.orElse(0);
		int pageSize = 5;

		// to get token from session
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");

		// list on post base on current page and page size
		CustomResponse customResponse = contactService.getListContactByEmail(token, email, currentPage, pageSize);
		Page<ContactGetDto> listPageImpl = convertService.convertToResponsePage(customResponse);

		model.addAttribute("postPage", listPageImpl);
		model.addAttribute("pageChoose", currentPage);

		return PAGE_LIST_CONTACT;
	}

	/*
	 * ==============================>to create new contact model: add attribute
	 * request: set utf response: set utf
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public String listPagination(Model model, HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {

		// set utf 8
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		// init contact
		model.addAttribute("contact", new ContactPostDto());
		return PAGE_CREATE_CONTACT;
	}

	@RequestMapping(value = "/saveContact", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
	public String save(RedirectAttributes redirAttrs, HttpServletRequest request,
			@ModelAttribute("contact") ContactPostDto contactPostDto) {

		// create contact base on token
		CustomResponse customResponse = contactService.createContact(contactPostDto);

		if (customResponse.getListErrorMessages() != null) {
			redirAttrs.addFlashAttribute("meesageError",
					customResponse.getListErrorMessages().get(0).getMessage().toString());
		} else {
			redirAttrs.addFlashAttribute("messageSuccess", customResponse.getMessage());
		}

		return "redirect:/contact/create";
	}
}
