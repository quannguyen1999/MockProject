package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.example.demo.model.CustomResponse;
import com.example.demo.service.CategoryService;
import com.example.demo.service.RestTemplateCallService;

@Service
@PropertySource("classpath:url.properties")
public class CategoryImpl implements CategoryService{

	//cal service restemplate
	@Autowired
	private RestTemplateCallService restTemplateCallService;

	//call url from env
	@Autowired
	private Environment env;

	//get list category from pagination
	@Override
	public CustomResponse getListCategory(String token, int page, int size) {
		return restTemplateCallService.getResponseWithToken(
				env.getProperty("url.localhost")+ env.getProperty("url.category.listPagination")+"?pageSize="+size+"&pageNo="+page, token);//,token);
	}

	//get category by id
	@Override
	public CustomResponse getCategoryById(String idCategory) {
		return restTemplateCallService.getResponse(
				env.getProperty("url.localhost")+ env.getProperty("url.category.detail")+idCategory);
	}

	//get list category
	@Override
	public CustomResponse getList() {
		return restTemplateCallService.getResponse(env.getProperty("url.localhost")+ env.getProperty("url.category.list"));
	}

	//get list category for NOMENCLATURE
	@Override
	public CustomResponse getListWithTypeMenuAndNOMENCLATUREAndCourse() {
		return restTemplateCallService.getResponse(env.getProperty("url.localhost")+ env.getProperty("url.category.list")+"?listStringsTypeCategory=POST,NOMENCLATURE,COURSE");
	}

	//get list category for home page
	@Override
	public CustomResponse getListCategoryHomePage() {
		return restTemplateCallService.getResponse(env.getProperty("url.localhost")+ env.getProperty("url.category.listHomePage"));
	}

	@Override
	public CustomResponse findAll(String token, String urlRequestParam) {
		return restTemplateCallService.getResponseWithToken(env.getProperty("url.localhost")+ env.getProperty("url.category.findAll")+urlRequestParam, token);
	}
}
