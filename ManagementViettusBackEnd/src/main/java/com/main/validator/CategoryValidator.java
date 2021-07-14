package com.main.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.main.model.CategoryPostDto;
import com.main.model.ErrorMessage;
import com.main.service.CategoryService;
import com.main.service.ResponseService;
import com.main.service.ValidatorService;


@Component
public class CategoryValidator {
	private static String NAME = "name";
	
	private static String ID = "id";

	@Autowired
	private ValidatorService validatorService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ResponseService responseService;
	
	public List<ErrorMessage> validatePostCategory(CategoryPostDto categoryPostDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = validatorService.checkBindingResult(result);
		if(listErrorResponses.size()>=1) {
			return listErrorResponses;
		}
		List<CategoryPostDto> listCategoryPostDtos = categoryService.findCategoryByName(categoryPostDto.getName());
		if(listCategoryPostDtos.size() >= 1) {
			listErrorResponses.add(new ErrorMessage(NAME,
					responseService.getMessage("category.name.conflix")));
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateIdCategory(String idCategory) {
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		int idCategoryConvert = 0;
		try {
			idCategoryConvert=Integer.parseInt(idCategory);
		} catch (Exception e) {
			// TODO: handle exception
			
				listErrorResponses.add(new ErrorMessage(ID,
						responseService.getMessage("category.id.invalid")));
				return listErrorResponses;
		}
		if(categoryService.findCategoryById(idCategoryConvert) == null) {
			listErrorResponses.add(new ErrorMessage(ID,
					responseService.getMessage("category.id.notFound")
					));
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateIdCategoryStatusIsTrue(String idCategory) {
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		int idCategoryConvert = 0;
		try {
			idCategoryConvert=Integer.parseInt(idCategory);
		} catch (Exception e) {
				listErrorResponses.add(new ErrorMessage(ID,
						responseService.getMessage("category.id.invalid"
						)));
				return listErrorResponses;
		}
		CategoryPostDto categoryPostDto = categoryService.findCategoryById(idCategoryConvert);
		if(categoryPostDto== null) {
			listErrorResponses.add(new ErrorMessage(ID,
					responseService.getMessage("category.id.notFound"
							)));
		}else if(categoryPostDto.getStatus() == false) {
			listErrorResponses.add(new ErrorMessage(ID,
					
					responseService.getMessage("category.status.isOff"
							)));
		}
		return listErrorResponses;
	}
	
	
}
