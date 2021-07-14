package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.AccountGetDto;
import com.example.demo.model.CategoryGetDto;
import com.example.demo.model.CategoryPostDto;
import com.example.demo.model.CourseGetDto;
import com.example.demo.model.CustomResponse;
import com.example.demo.model.PostPostDto;
import com.example.demo.model.TypeCategory;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ConvertService;
import com.example.demo.service.CourseService;
import com.example.demo.service.PostService;
import com.example.demo.service.RestTemplateCallService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/category")
public class CategoryController {
	//file source html
	private static final String PAGE_LIST_CATEGORY = "manageCategory/listCategory";
	private static final String PAGE_DETAIL_CATEGORY = "manageCategory/detailCategory";
	private static final String PAGE_LIST_GLOSSARY = "manageGlossay/listGlossary";
	private static final String PAGE_LIST_COURSE = "manageCategory/detailCategoryCourse";
	private static final String PAGE_LIST_DETAIL_GLOSSARY = "manageGlossay/detailGlossary";

	//to call service
	@Autowired
	private CategoryService categoryService;

	//to call service
	@Autowired
	private PostService postService;

	@Autowired
	private ConvertService convertService;

	@Autowired
	private CourseService courseService;

	/*
	 * ========================>list category by page
	 * model: to add attribute
	 * request: to get session
	 * page: to get page choose
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String listPagination( 
			Model model, 
			HttpServletRequest request,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("urlRequest") Optional<String> urlRequest
			) {
		CustomResponse customResponse;
		Page<CategoryGetDto> listPageImpl = null;
		
		//get current page and page size
		int currentPage = page.orElse(0);
		int pageSize = 5;

		//to get token
		HttpSession session =request.getSession();
		String token = (String) session.getAttribute("token");

		//list all category by current page and page size
		String getUrlRequest = urlRequest.orElse(null);
		if(getUrlRequest != null) {
			getUrlRequest = getUrlRequest.replace("$", "&");
			
			customResponse = categoryService.findAll(token, "?page="+
					currentPage+"&size="+pageSize+"&"+getUrlRequest);
			
			model.addAttribute("getUrlRequest", getUrlRequest);
			
			listPageImpl = convertService.convertToResponsePage(customResponse);
		}else {
			//list account by current page and page size
			customResponse = categoryService.getListCategory(token,currentPage, pageSize);
			listPageImpl = convertService.convertToResponsePage(customResponse);
		
		}

		model.addAttribute("categoryPage", listPageImpl);

		//to get current page choose
		model.addAttribute("pageChoose", currentPage);

		return PAGE_LIST_CATEGORY;
	}

	/*
	 * ==============================>return detail category by id category and page choose or return page category glossary
	 *model: add attribute
	 *request: to get session
	 *id: to get id category
	 *page: page choose
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String getDetailCategoryById(
			Model model, 
			HttpServletRequest request,
			@RequestParam("id") String id,
			@RequestParam("page") Optional<Integer> page
			) {
		//page return default
		String pageReturn = PAGE_DETAIL_CATEGORY;

		//get current page and page size default
		int currentPage = page.orElse(0);
		int pageSize = 5;

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

			/*
			 * NOT FIX CLEAR CODE
			 * */
			CustomResponse customResponse2 = postService.getListPostWithIdCategory(currentPage, pageSize,categoryPostDto.getIdCategory());
			listPageImpl = convertService.convertToResponsePage(customResponse2);
			model.addAttribute("idPage", id);
			model.addAttribute("postPage", listPageImpl);
			model.addAttribute("pageChoose", currentPage);

			//if typeCategory is NOMENCLATURE then return page list glossary
			if(categoryPostDto.getTypeCategory() != null && categoryPostDto.getTypeCategory().toString() == TypeCategory.NOMENCLATURE.toString()) {
				pageReturn = PAGE_LIST_GLOSSARY;
			}else if(categoryPostDto.getTypeCategory() != null && categoryPostDto.getTypeCategory().toString().equalsIgnoreCase(TypeCategory.COURSE.toString())){
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

				pageReturn = PAGE_LIST_COURSE;
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

	/*=============================>detail glossary by id cateogry and page choose and name start with
	 * model: add attribute
	 * request: set attribute
	 * id: to get id category
	 * page: page choose
	 * name: title start post with character from input client
	 * */
	@RequestMapping(value = "/detail/glossary", method = RequestMethod.GET)
	public String getDetailGlossaryByIdCategory(
			Model model, 
			HttpServletRequest request,
			@RequestParam("id") Integer id,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam(defaultValue = "A") String name
			) {
		//get current page and page size default
		int currentPage = page.orElse(0);
		int pageSize = 5;

		//to find category by id
		CustomResponse customResponse = categoryService.getCategoryById(String.valueOf(id));

		//list post by id category and name post must start with ..
		CustomResponse customResponse2 = postService.getListPostWithIdCategoryAndNamePostStartWith(currentPage, pageSize,id,name);
		PageImpl<PostPostDto> listPageImpl = convertService.convertToResponsePage(customResponse2);
		model.addAttribute("postPage", listPageImpl);
		model.addAttribute("pageChoose", currentPage);

		//if no exists return message error
		if( customResponse.getListErrorMessages() != null && customResponse.getListErrorMessages().size() >= 1) {
			request.setAttribute("meesageError","Không có danh sách nào");
		}else {
			//otherwise if exists then convert object to CategoryPostDto
			CategoryPostDto categoryPostDto = null;
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			categoryPostDto = objectMapper.convertValue(customResponse.getData(), CategoryPostDto.class);

			//if typeCategory is NOMENCLATURE then add attribute
			if(categoryPostDto.getTypeCategory().toString().equalsIgnoreCase(TypeCategory.NOMENCLATURE.toString())) {
				model.addAttribute("category", categoryPostDto);
			}
		}
		return PAGE_LIST_DETAIL_GLOSSARY;
	}
}
