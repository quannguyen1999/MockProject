package com.main.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.main.entity.Post;
import com.main.entity.TypePost;
import com.main.model.PostPostDto;
import com.querydsl.core.types.Predicate;

public interface PostService {
	Boolean savePost(PostPostDto postPostDto);

	PostPostDto findPostById(int id);

	Post findPostByIdAndReturnPost(int id);

	PostPostDto findPostByIdNotMenu(int id);

	Boolean deletePostById(int id);

	PostPostDto updatePost(PostPostDto postPostDto, int idPost);

	List<PostPostDto> list();

	PageImpl<PostPostDto> listPagination(Integer pageNo, Integer pageSize);

	PageImpl<PostPostDto> listPaginationByUserName(Integer pageNo, Integer pageSize, String username);

	PageImpl<PostPostDto> listPaginationWithIdPostAndStatusTrue(Integer pageNo, Integer pageSize, int idCategory);

	PageImpl<PostPostDto> listPaginationWithIdCategoryAndStatusTrue(Integer pageNo, Integer pageSize, int idCategory);

	PageImpl<PostPostDto> listPaginationWithIdCategoryAndStatusTrueAndNameStartWith(Integer pageNo, Integer pageSize, int idCategory, String name);

	List<PostPostDto> findByIdCategoryAndTypePost(int idCategory, TypePost typePost);

	List<PostPostDto> listAllPostTypeMenuAndCategoryIsCourseAndCategoryStatusIsTrue();

	PostPostDto detailPostGlossary(int idPostGlossary);

	PostPostDto findPostByIdTypeMenuAndCategoryIsCourse(Integer idPost);

	//list Post version 2
	PageImpl<PostPostDto> findAll(Predicate predicate, Pageable pageable);
	
	//find by name
	PageImpl<PostPostDto> findName(String name, Pageable pageable);
}
