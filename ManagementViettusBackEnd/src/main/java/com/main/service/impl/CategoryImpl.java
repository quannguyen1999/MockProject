package com.main.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.main.entity.Category;
import com.main.entity.TypeCategory;
import com.main.entity.TypePost;
import com.main.mapstruct.CategoryMapper;
import com.main.mapstruct.PostMapper;
import com.main.model.CategoryGetDto;
import com.main.model.CategoryPostDto;
import com.main.model.CategoryPutDto;
import com.main.model.PostPostDto;
import com.main.repository.CategoryRepository;
import com.main.repository.PostRepository;
import com.main.service.CategoryService;
import com.querydsl.core.types.Predicate;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@AllArgsConstructor
@Service
public class CategoryImpl implements CategoryService{

	private final CategoryRepository categoryRepository;
	
	private final CategoryMapper categoryMapper;
	
	private final PostMapper postMapper;
	
	private final PostRepository postRepository;
	
	@Override
	public CategoryPostDto findCategoryById(int id) {
		Optional<Category> cateOptional = categoryRepository.findById(id);
		return cateOptional.isPresent() ? categoryMapper.categoryToCategoryGetDto(cateOptional.get()) : null;
	}

	@Override
	public Boolean saveCategory(CategoryPostDto categoryDto) {
		try {
			if(categoryDto.getTypeCategory() == null) {
				categoryDto.setTypeCategory(TypeCategory.POST);
			}
			categoryDto.setStatus(true);
			if(categoryDto.getIdCategory() == 0) {
				categoryDto.setIdCategory(
						Integer.parseInt(String.valueOf(categoryRepository.count()))+1);
			}
			categoryRepository.save(categoryMapper.categoryGetDtoToCategory(categoryDto));
		
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean deleteCategory(int id) {
		try {
			categoryRepository.deleteById(id);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<CategoryPostDto> listCategoryDtos() {
		return categoryMapper.listCategoryToListCategoryGetDto(categoryRepository.findByStatusTrue());
	}

	@Override
	public List<CategoryPostDto> findCategoryByName(String name) {
		return categoryMapper.listCategoryToListCategoryGetDto(categoryRepository.findByName(name));
	}

	@Cacheable(value = "listCategory",key="#pageNo")
	@Override
	public PageImpl<CategoryPostDto> listPagination(Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Category> page =  categoryRepository.findAll(paging);
		return returnPage(page, paging);
	}

	@Override
	public CategoryPostDto updateCategory(CategoryPutDto categoryPutDto, int idCategory) {
		CategoryPostDto caPostDtoCurrent = findCategoryById(idCategory);
		if(categoryPutDto.getContent() != null && categoryPutDto.getContent().isEmpty() == false) {
			caPostDtoCurrent.setContent(categoryPutDto.getContent());
		}
		if(categoryPutDto.getStatus() != null) {
			caPostDtoCurrent.setStatus(categoryPutDto.getStatus());
		}
		if(categoryPutDto.getTypeCategory() != null) {
			caPostDtoCurrent.setTypeCategory(categoryPutDto.getTypeCategory());
		}
		return categoryMapper.categoryToCategoryGetDto(categoryRepository.save(categoryMapper.categoryGetDtoToCategory(caPostDtoCurrent)));
	}

	@Override
	public List<CategoryPostDto> listCategoryWithoutProgramming() {
		Collection<Integer> listCollection = new HashSet<Integer>();
		listCollection.add(0);
		return categoryMapper.listCategoryToListCategoryGetDto(categoryRepository.findByIdCategoryNotIn(listCollection));
	}

	@Override
	public List<CategoryGetDto> listCategoryDtosForHeader() {
		List<Category> listCategories = categoryRepository.findByStatusTrue();
		List<CategoryGetDto> listCategoryGetDtos = categoryMapper.listCategoryToListCategoryGetDtoForHeader(listCategories);
		listCategoryGetDtos.forEach(t->{
			List<PostPostDto> listPosts = postMapper.listPostToListPostDto(postRepository.findByCategoryIdCategoryAndCategoryStatusIsTrueAndTypePostAndStatusIsTrue(t.getIdCategory(), TypePost.MENU));
			t.setListPost(listPosts);
		});
		return listCategoryGetDtos;
	}

	@Override
	public List<CategoryPostDto> listAllCategoryByListTypeCategory(List<String> typeCategories) {
		List<TypeCategory> listTypeCategories =typeCategories
		        .stream()
		        .map(TypeCategory::valueOf)
		        .collect(Collectors.toList());
		List<Category> listCategories = categoryRepository.findByStatusTrueAndTypeCategoryIn(listTypeCategories);
		return categoryMapper.listCategoryToListCategoryGetDto(listCategories);
	}

	@Override
	public List<CategoryGetDto> listCategoryDtosForHomePage() {
		List<Category> listCategories = categoryRepository.findByStatusTrue();
		List<CategoryGetDto> listCategoryGetDtos = categoryMapper.listCategoryToListCategoryGetDtoForHeader(listCategories);
		listCategoryGetDtos.forEach(t->{
			List<PostPostDto> listPosts = postMapper.listPostToListPostDto(postRepository.findTop4ByCategoryIdCategoryAndTypePostAndStatusIsTrue(t.getIdCategory(), TypePost.POST));
			t.setListPost(listPosts);
		});
		return listCategoryGetDtos;
	}
	
	@Override
	public PageImpl<CategoryPostDto> findAll(Predicate predicate, Pageable pageable) {
		Page<Category> page = categoryRepository.findAll(predicate, pageable);
		return returnPage(page, pageable);
	}
	
	private PageImpl<CategoryPostDto> returnPage(Page<Category> page,Pageable paging){
		return new PageImpl<CategoryPostDto>(
				page.getContent().stream().map(category->
				new CategoryPostDto(category.getIdCategory(),category.getName(), category.getContent(), category.getStatus(), category.getTypeCategory())).collect(Collectors.toList()),
				paging, page.getTotalElements()
				);
	}



}
