package com.example.demo.service;

import com.example.demo.model.CustomResponse;

public interface CategoryService {
	//Page<CategoryGetDto>
	CustomResponse getListCategory(String token, int page, int size);

	//CategoryGetDto
	CustomResponse getCategoryById(String idCategory);

	//List<CategoryGetDto>
	CustomResponse getList();

	//List<CategoryGetDto>
	CustomResponse getListWithTypeMenuAndNOMENCLATUREAndCourse();

	//List<CategoryGetDto>
	CustomResponse getListCategoryHomePage();

	//Page<CategoryGetDto> version 2
	CustomResponse findAll(String token, String urlRequestParam);
}
