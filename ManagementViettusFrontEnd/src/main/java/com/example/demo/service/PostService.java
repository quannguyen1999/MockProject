package com.example.demo.service;


import com.example.demo.model.CustomResponse;
import com.example.demo.model.PostPostDto;

public interface PostService {
	public CustomResponse createPost(PostPostDto postDto, String token);

	//List<PostPostDto>
	public CustomResponse getListPostCourse(String token);

	//Page<PostPostDto>
	public CustomResponse getListPost(String token, int page, int size);

	//Page<PostPostDto>
	public CustomResponse getListPostWithIdCategory(int page, int size, int idCategory);

	//Post<PostPostDto>
	public CustomResponse getListPostWithIdPost(int page, int size, int idPost);

	//Post<PostPostDto>
	public CustomResponse getListPostWithIdCategoryAndNamePostStartWith(int page, int size, int idCategory, String name);

	//PostPostDto
	public CustomResponse getDetailPost(int idPost);

	//PostPostDto
	public CustomResponse getDetailPostGlossary(int idPost);

	//List<PostPostDto>
	public CustomResponse listPostMenuByIdCategory(int idCategory);

	//Page<PostPostDto> version 2
	CustomResponse findAll(String token, String urlRequestParam);

	//Page<PostPostDto> version 2
	CustomResponse findName(String urlRequestParam);

}
