package com.main.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.main.entity.Category;
import com.main.entity.TypeCategory;
import com.main.model.CategoryPostDto;
import com.main.model.CategoryPutDto;
import com.main.model.ErrorMessage;
import com.main.service.CategoryService;
import com.main.service.ResponseService;
import com.main.validator.CategoryValidator;
import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping(CategoryController.API_CATEGORY)
public class CategoryController {

	//url chung cho category
	public final static String API_CATEGORY = "/api/v1/category";

	//default token
	static final String DEFAULT_ACCESS_TOKEN_HEADER = "AccessToken";

	//để crud
	@Autowired
	private CategoryService categoryService;

	//để validate dữ liệu
	@Autowired
	private CategoryValidator categoryValidator;
	
	//trả về custom response và get message từ url
	@Autowired
	private ResponseService responseService;
	
	//find all version 2
	@GetMapping(value = "/findAll")
	public Object findAll(@QuerydslPredicate(root = Category.class) Predicate predicate,
			Pageable pageable) {
		return responseService.getResponseCustom("request.updateSuccess", categoryService.findAll(predicate, pageable), HttpStatus.OK);
	}

	//trả về list category qua danh sách typeCategory
	@GetMapping(value = "/list")
	public Object getList(@RequestParam(defaultValue = "none") List<String> listStringsTypeCategory) {
		Set<String> values = Arrays.stream(TypeCategory.values())
                .map(TypeCategory::toString)
                .collect(Collectors.toSet());
		if(listStringsTypeCategory.stream().anyMatch(values::contains)) {
			return responseService.getResponseCustom("request.getListSuccess", 	
					categoryService.listAllCategoryByListTypeCategory(listStringsTypeCategory), 
					HttpStatus.OK);
		}
		return responseService.getResponseCustom("request.getListSuccess", 	
				categoryService.listCategoryDtos(), HttpStatus.OK);
	}

	//trả về danh sách category header cho client
	@GetMapping(value = "/listHeader")
	public Object getListHeader() {
		return responseService.getResponseCustom("request.getListSuccess", 	
				categoryService.listCategoryDtosForHeader(), HttpStatus.OK);
	}
	
	//trả về danh sách category homepage cho client
	@GetMapping(value = "/listHomePage")
	public Object getListHomePage() {
		return responseService.getResponseCustom("request.getListSuccess", 	
				categoryService.listCategoryDtosForHomePage(), HttpStatus.OK);
	}

	//chi tiết category thông qua id
	@GetMapping(value = "/detail")
	public Object getDetailCategoryById(
			@RequestParam(defaultValue = "1") String id) {
		List<ErrorMessage> listErrorResponses = categoryValidator.validateIdCategoryStatusIsTrue(id);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		return responseService.getResponseCustom("request.getDetailSuccess", 	
				categoryService.findCategoryById(Integer.parseInt(id)), HttpStatus.OK);
	}

	//phân trang cho category, nhưng cho admin
	@GetMapping(value = "/listPagination")
	public Object getListCategoryPagination(
			@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "2") Integer pageSize
			){
		return responseService.getResponseCustom("request.getListSuccess", 	
				categoryService.listPagination(pageNo,pageSize), HttpStatus.OK);
	}

	//tạo category
	@PostMapping(value = "/create")
	public Object createCategory(@RequestBody CategoryPostDto categoryDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = categoryValidator.validatePostCategory(categoryDto, result);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		categoryService.saveCategory(categoryDto);
		return responseService.getResponseCustom("request.createSuccess", 	
				null, HttpStatus.CREATED);
	}
	
	//xóa category qua id
	@DeleteMapping(value = "/delete")
	public Object createCategory(@Param("id") String id) {
		List<ErrorMessage> listErrorResponses = categoryValidator.validateIdCategory(id);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		categoryService.deleteCategory(Integer.parseInt(id));
		return responseService.getResponseCustom("request.deleteSuccess", 	
				null, HttpStatus.OK);
	}

	//cập nhập category qua id
	@PutMapping(value = "/update")
	public Object createCategory(@Param("id") String id,
			@RequestBody CategoryPutDto categoryPutDto) {
		List<ErrorMessage> listErrorResponses = categoryValidator.validateIdCategory(id);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		};
		return responseService.getResponseCustom("request.updateSuccess", 	
				categoryService.updateCategory(categoryPutDto,Integer.parseInt(id)), HttpStatus.OK);
	}
}
