package com.example.demo.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.example.demo.model.CustomResponse;
import com.example.demo.model.RegistrationPostDto;
import com.example.demo.service.RegistrationService;
import com.example.demo.service.RestTemplateCallService;

@Service
public class RegistrationImpl implements RegistrationService{

	//cal service restemplate
	@Autowired
	private RestTemplateCallService restTemplateCallService;

	//call message from env
	@Autowired
	private Environment env;

	@Override
	public CustomResponse register(RegistrationPostDto registrationPostDto) {
		return restTemplateCallService.postResponse(registrationPostDto,
				env.getProperty("url.localhost")+ env.getProperty("url.course.register"));
	}

	@Override
	public CustomResponse getListRegistration(String token, int page, int size) {
		return restTemplateCallService.getResponseWithToken(env.getProperty("url.localhost")+ env.getProperty("url.registration.list")+"?pageSize="+size+"&pageNo="+page, token);
	}

	@Override
	public CustomResponse getListRegistrationByidCourse(Integer idCourse) {
		return restTemplateCallService.getResponse(env.getProperty("url.localhost")+ env.getProperty("url.registration.listByIdCourse")+"?idCourse="+idCourse);//+"&pageNo="+page, token);
	}

	@Override
	public CustomResponse findAll(String token, String urlRequestParam) {
		System.out.println(env.getProperty("url.localhost")+ env.getProperty("url.registration.findAll")+urlRequestParam);
		return restTemplateCallService.getResponseWithToken(env.getProperty("url.localhost")+ env.getProperty("url.registration.findAll")+urlRequestParam, token);
	}
}
