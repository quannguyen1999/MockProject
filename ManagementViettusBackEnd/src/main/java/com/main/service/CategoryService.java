package com.main.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.main.model.CategoryGetDto;
import com.main.model.CategoryPostDto;
import com.main.model.CategoryPutDto;
import com.querydsl.core.types.Predicate;

public interface CategoryService {
	//find category by id
	public CategoryPostDto findCategoryById(int id);

	//save category
	public Boolean saveCategory(CategoryPostDto categoryDto);

	//delete category by id 
	public Boolean deleteCategory(int id);

	//list category by pagination
	public List<CategoryPostDto> listCategoryDtos();

	//for client, but just list for header
	public List<CategoryGetDto> listCategoryDtosForHeader();

	//list homePage
	public List<CategoryGetDto> listCategoryDtosForHomePage();

	//list category without list programing
	public List<CategoryPostDto> listCategoryWithoutProgramming();

	//find category by name
	public List<CategoryPostDto> findCategoryByName(String name);

	//update category
	public CategoryPostDto updateCategory(CategoryPutDto categoryPutDto, int idCategory);

	//list pagination
	PageImpl<CategoryPostDto> listPagination(Integer pageNo, Integer pageSize);

	//list all category by type category
	public List<CategoryPostDto> listAllCategoryByListTypeCategory(List<String> typeCategories);

	//list Category and query
	PageImpl<CategoryPostDto> findAll(Predicate predicate, Pageable pageable);
}
