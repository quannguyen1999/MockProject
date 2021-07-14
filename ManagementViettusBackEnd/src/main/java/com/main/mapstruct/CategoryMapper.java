package com.main.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.main.entity.Category;
import com.main.model.CategoryGetDto;
import com.main.model.CategoryPostDto;

@Mapper(
        componentModel = "spring"
)
public interface CategoryMapper {
	Category categoryGetDtoToCategory(CategoryPostDto categoryGetDto);

	CategoryPostDto categoryToCategoryGetDto(Category category);

	List<CategoryPostDto> listCategoryToListCategoryGetDto(List<Category> listCategories);
	
	@Mapping(target = "listPost", ignore = true)
	List<CategoryGetDto> listCategoryToListCategoryGetDtoForHeader(List<Category> listCategories);
}