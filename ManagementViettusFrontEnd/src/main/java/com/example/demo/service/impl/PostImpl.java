package com.example.demo.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.example.demo.model.CustomResponse;
import com.example.demo.model.PostPostDto;
import com.example.demo.service.PostService;
import com.example.demo.service.RestTemplateCallService;

@Service
@PropertySource("classpath:url.properties")
public class PostImpl implements PostService{

	//cal service restemplate
	@Autowired
	private RestTemplateCallService restTemplateCallService;

	//call message from env
	@Autowired
	private Environment env;

	//create post
	@Override
	public CustomResponse createPost(PostPostDto postDto, String token) {
		return restTemplateCallService.postResponseWithToken(postDto,
				env.getProperty("url.localhost")+ env.getProperty("url.post.create")
				,token);
	}

	@Override
	public CustomResponse getListPost(String token, int page, int size) {
		return restTemplateCallService.getResponseWithToken(
				env.getProperty("url.localhost")+ env.getProperty("url.post.listPagination")+"?pageSize="+size+"&pageNo="+page,token);
	}

	@Override
	public CustomResponse getListPostWithIdCategory(int page, int size, int idCategory) {
		return restTemplateCallService.getResponse(
				env.getProperty("url.localhost") + env.getProperty("url.post.listPaginationNoAuth")+"?pageSize="+size+"&pageNo="+page+"&idCategory="+idCategory);
	}

	@Override
	public CustomResponse getDetailPost(int idPost) {
		return restTemplateCallService.getResponse(env.getProperty("url.localhost")+ env.getProperty("url.post.detail")+"?idPost="+idPost);
	}

	@Override
	public CustomResponse listPostMenuByIdCategory(int idCategory) {
		return restTemplateCallService.getResponse(env.getProperty("url.localhost")+ env.getProperty("url.post.listByIdCategoryAndTypePost")+"?idCategory="+idCategory);
	}

	@Override
	public CustomResponse getListPostWithIdPost(int page, int size, int idPost) {
		return restTemplateCallService.getResponse(
				env.getProperty("url.localhost")+ env.getProperty("url.post.listPaginationNoAuth")+"?pageSize="+size+"&pageNo="+page+"&idPost="+idPost);
	}

	@Override
	public CustomResponse getListPostWithIdCategoryAndNamePostStartWith(int page, int size,
			int idCategory, String name) {
		return restTemplateCallService.getResponse(env.getProperty("url.localhost")+ env.getProperty("url.post.findCategoryGlossaryWithNameStarting")+"?pageSize="+size+"&pageNo="+page+"&idCategory="+idCategory+"&name="+name);
	}

	@Override
	public CustomResponse getDetailPostGlossary(int idPost) {
		return restTemplateCallService.getResponse(
				env.getProperty("url.localhost")+ env.getProperty("url.post.detailGlossary")+"?idPost="+idPost);
	}

	@Override
	public CustomResponse getListPostCourse(String token) {
		return restTemplateCallService.getResponseWithToken(
				env.getProperty("url.localhost")+ env.getProperty("url.post.listPostCourse"),token);
	}

	@Override
	public CustomResponse findAll(String token, String urlRequestParam) {
		return restTemplateCallService.getResponseWithToken(env.getProperty("url.localhost")+ env.getProperty("url.post.findAll")+urlRequestParam, token);
		}

	@Override
	public CustomResponse findName(String urlRequestParam) {
		return restTemplateCallService.getResponse(env.getProperty("url.localhost")+ env.getProperty("url.post.findName")+urlRequestParam);
	}

}
