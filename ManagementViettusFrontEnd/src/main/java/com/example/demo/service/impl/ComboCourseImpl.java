package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.demo.model.CustomResponse;
import com.example.demo.service.ComboCourseService;
import com.example.demo.service.RestTemplateCallService;

@Service
public class ComboCourseImpl implements ComboCourseService{

	//cal service restemplate
	@Autowired
	private RestTemplateCallService restTemplateCallService;

	//call url from env
	@Autowired
	private Environment env;

	@Override
	public CustomResponse listComboCoursesByIdPostCourse(int idPost) {
		CustomResponse customResponse = restTemplateCallService.getResponse(env.getProperty("url.localhost")+ env.getProperty("url.comboCourse.ListAllByIdPost")+"?idPost="+idPost);
		return customResponse;
	}

	@Override
	public CustomResponse findAll(String token, String urlRequestParam) {
		return restTemplateCallService.getResponseWithToken(env.getProperty("url.localhost")+ env.getProperty("url.comboCourse.findAll")+urlRequestParam, token);
	}

}
