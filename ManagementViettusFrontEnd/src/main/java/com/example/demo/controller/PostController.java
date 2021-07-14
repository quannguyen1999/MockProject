package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.AccountGetDto;
import com.example.demo.model.CategoryGetDto;
import com.example.demo.model.CategoryPostDto;
import com.example.demo.model.ComboCourseGetDto;
import com.example.demo.model.CourseGetDto;
import com.example.demo.model.CustomResponse;
import com.example.demo.model.PostPostDto;
import com.example.demo.model.TypeCategory;
import com.example.demo.model.TypePost;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ComboCourseService;
import com.example.demo.service.ConvertService;
import com.example.demo.service.CourseService;
import com.example.demo.service.PostService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/post")
public class PostController{
	//source page html
	private static final String PAGE_LIST_POST = "managePost/listPost";
	private static final String PAGE_CREATE_POST = "managePost/createPost";
	private static final String PAGE_DETAIL_POST = "managePost/detailPost";
	private static final String PAGE_DETAIL_POST_GLOSSARY = "manageGlossay/detailPostGlossary";
	private static final String PAGE_DETAIL_POST_COURSE = "managePost/detailPostCourse";
	private static final String PAGE_SEARCH_POST = "managePost/searchPost";
	
	//to cal service
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private PostService postService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ConvertService convertService;
	
	@Autowired
	private ComboCourseService comboCourseService;

	/*==============================>to create new post
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
		//set utf 8
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		//list all category glossary
		CustomResponse customResponse = categoryService.getListWithTypeMenuAndNOMENCLATUREAndCourse();
		if(customResponse.getListErrorMessages() != null) {
			request.setAttribute("meesageError", customResponse.getListErrorMessages().get(0).getMessage());
		}else {
			//convert object to List<CategoryPostDto>
			List<CategoryPostDto> categoryPostDtos = null;
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			categoryPostDtos= objectMapper.convertValue(customResponse.getData(),objectMapper.getTypeFactory().constructCollectionLikeType(List.class, CategoryPostDto.class));
			model.addAttribute("listCategory",categoryPostDtos);
		}
		
		//check if had message before request from function addFlashAttribute
		if(model.containsAttribute("messageSuccess") == true)
			request.setAttribute("messageSuccess", request.getAttribute("messageSuccess"));
		if(model.containsAttribute("meesageError") == true)
			request.setAttribute("meesageError", request.getAttribute("meesageError"));
		
		//init post
		model.addAttribute("post", new PostPostDto());
		return PAGE_CREATE_POST;
	}

	/*===================================>process save post
	 * redirAttrs: to set flash attribute
	 * request: to get session
	 * postPostDto: to get value
	*/
	@RequestMapping(value = "/savePost", method = RequestMethod.POST,produces = "text/html; charset=UTF-8")
	public String listPagination(
			RedirectAttributes redirAttrs,
			HttpServletRequest request,
			@ModelAttribute("post") PostPostDto postPostDto
			) {
		//get session
		HttpSession session = request.getSession();
		
		//create post base on token
		CustomResponse customResponse = postService.createPost(postPostDto,(String)session.getAttribute("token"));
		
		if(customResponse.getListErrorMessages() != null) {
			redirAttrs.addFlashAttribute("meesageError", customResponse.getListErrorMessages().get(0).getMessage().toString());
		}else {
			redirAttrs.addFlashAttribute("messageSuccess", customResponse.getMessage());
		}
		
		return "redirect:/post/create";
	}

	/*
	 * ====================================>detail post by id and page choose
	 * id: id post
	 * model: add attribute
	 * request: to set attribute
	 * page: to get current page choose
	 * */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detailPost(
			@RequestParam(defaultValue = "1") Optional<Integer> id,
			Model model, 
			HttpServletRequest request,
			@RequestParam("page") Optional<Integer> page
			) {
		String pageReturnDefault = PAGE_DETAIL_POST;
		
		//current page choose and pageSize default
		int currentPage = page.orElse(0);
		int pageSize = 5;
		
		//get detail post by id
		CustomResponse customResponse = postService.getDetailPost(id.get());
		
		if(customResponse.getListErrorMessages() != null) {
			model.addAttribute("meesageError", customResponse.getListErrorMessages().get(0).getMessage().toString());
		}else {
			//if exists then convert object to PostPostDto
			PostPostDto postPostDto = null;
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			postPostDto = objectMapper.convertValue(customResponse.getData(), PostPostDto.class);
			
			//add attribute
			model.addAttribute("post",postPostDto);
			CustomResponse cusResponseCategory = categoryService.getCategoryById(String.valueOf(postPostDto.getIdCategory()));
			CategoryPostDto categoryPostDto = convertService.convertToObject(cusResponseCategory, new CategoryPostDto());
			if(categoryPostDto.getTypeCategory().toString().equalsIgnoreCase(TypeCategory.COURSE.toString())) {
				CustomResponse customResponseListCourseByIdPostTypeMenu = courseService.getListCourseByidPostWithStatusIsTrue(postPostDto.getIdPost());
				
				if(customResponseListCourseByIdPostTypeMenu.getListErrorMessages() != null) {
					
				}else {
					List<CourseGetDto> listCourseGetDtos = convertService.convertToList(customResponseListCourseByIdPostTypeMenu, new CourseGetDto());
					
					model.addAttribute("listCourse", listCourseGetDtos);
				}
				
				CustomResponse customResponseComboCourse = comboCourseService.listComboCoursesByIdPostCourse(postPostDto.getIdPost());
				model.addAttribute("listComboCourse", customResponseComboCourse.getListErrorMessages() == null ? convertService.convertToList(customResponseComboCourse, new ComboCourseGetDto()) : null);
				
				pageReturnDefault = PAGE_DETAIL_POST_COURSE;
			
			}else {
				//to get list post relate
				CustomResponse cusResponse = null;
				PageImpl<PostPostDto> listPageImpl = null;
				
				//if type of post is menu, then find all list post relate base on id post
				if(postPostDto.getTypePost() == TypePost.MENU) {
					cusResponse = postService.getListPostWithIdPost(currentPage,pageSize,postPostDto.getIdPost());
				}else if(postPostDto.getIdPostSelf() != null) {
					//otherwise base on id Post Self
					cusResponse = postService.getListPostWithIdPost(currentPage,pageSize,postPostDto.getIdPostSelf());
				}else {
				}
				
				listPageImpl = convertService.convertToResponsePage(cusResponse);
				
				model.addAttribute("postPage", listPageImpl);
				model.addAttribute("pageChoose", currentPage);
			}
			
			//list all menu post base on id category
			CustomResponse customResponse2 =  postService.listPostMenuByIdCategory(postPostDto.getIdCategory());
			List<PostPostDto> listPostDtos = convertService.convertToList(customResponse2, new PostPostDto());
			model.addAttribute("listSubCategory",listPostDtos);
		}
		
		return pageReturnDefault;
	}

	/*
	 * ======================>get list post for admin, collabarator
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
		Page<PostPostDto> listPageImpl = null;
		
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
			
			customResponse = postService.findAll(token, "?page="+
					currentPage+"&size="+pageSize+"&"+getUrlRequest);
			
			model.addAttribute("getUrlRequest", getUrlRequest);
			
			listPageImpl = convertService.convertToResponsePage(customResponse);
		}else {
			//list account by current page and page size
			customResponse = postService.getListPost(token,currentPage, pageSize);
			listPageImpl = convertService.convertToResponsePage(customResponse);
		
		}
		
		model.addAttribute("postPage", listPageImpl);
		model.addAttribute("pageChoose", currentPage);
		return PAGE_LIST_POST;
	}
	
	/*
	 * ======================>search post by name for all user
	 * model: add attribute
	 * request: set attribute
	 * page: get current page choose
	 * */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String listPagination(
			Model model,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("urlRequest") Optional<String> urlRequest
			) {
		CustomResponse customResponse;
		Page<PostPostDto> listPageImpl = null;
		
		//to get current page and pageSize default
		int currentPage = page.orElse(0);
		int pageSize = 5;
		
		//list on post base on current page and page size

		String getUrlRequest = urlRequest.orElse(null);
		if(getUrlRequest != null) {
			model.addAttribute("nameSearch", getUrlRequest);
			getUrlRequest = getUrlRequest.replace(" ", "-");
			getUrlRequest = "&name="+getUrlRequest;
			customResponse = postService.findName( "?page="+
					currentPage+"&size="+pageSize+"&"+getUrlRequest);
			
			model.addAttribute("getUrlRequest", getUrlRequest);
		}else {
			//list account by current page and page size
			customResponse = postService.findName( "?page="+
					currentPage+"&size="+pageSize);
			listPageImpl = convertService.convertToResponsePage(customResponse);
		}
		
		listPageImpl = convertService.convertToResponsePage(customResponse);
		
		model.addAttribute("postPage", listPageImpl);
		model.addAttribute("pageChoose", currentPage);
		return PAGE_SEARCH_POST;
	}
	
	/*
	 * ====================>get detail Glossary base on id Post and page choose
	 * model: add attribute
	 * request: set attribute
	 * id: id post
	 * page: get current page choose
	 * */
	@RequestMapping(value = "detailGlossary", method = RequestMethod.GET)
	public String getDetailGlossary(
			Model model, 
			HttpServletRequest request,
			@RequestParam("id") Integer id,
			@RequestParam("page") Optional<Integer> page
			) {
		//to get current page and pageSize default
		int currentPage = page.orElse(0);
		int pageSize = 5;
		
		//find post glossary by id
		CustomResponse customResponse = postService.getDetailPostGlossary(id);
		PostPostDto postPostDto;
		//if no exists then return error
		if(customResponse.getListErrorMessages() != null && customResponse.getListErrorMessages().size() >= 1) {
			model.addAttribute("meesageError", customResponse.getListErrorMessages().get(0).getMessage().toString());
		}else {
			//otherwise convert object to PostPostDto
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			postPostDto = objectMapper.convertValue(customResponse.getData(), PostPostDto.class);
			model.addAttribute("post",postPostDto);

			//find category by id
			CustomResponse cusResponseCategory = categoryService.getCategoryById(String.valueOf(postPostDto.getIdCategory()));
			CategoryPostDto categoryPostDto = convertService.convertToObject(cusResponseCategory, new CategoryPostDto());
			
			//set name category
			model.addAttribute("category", categoryPostDto);
			model.addAttribute("nameCategory", postPostDto.getNameCategory());
			
			//get all list post relate base on id category and current page and page size
			CustomResponse cusResponse = postService.getListPostWithIdCategory(currentPage, pageSize,postPostDto.getIdCategory());
			PageImpl<PostPostDto> listPageImpl = convertService.convertToResponsePage(cusResponse);
			
			model.addAttribute("idPage", id);
			model.addAttribute("postPage", listPageImpl);
			model.addAttribute("pageChoose", currentPage);
		}
		return PAGE_DETAIL_POST_GLOSSARY;
	}
	



}
