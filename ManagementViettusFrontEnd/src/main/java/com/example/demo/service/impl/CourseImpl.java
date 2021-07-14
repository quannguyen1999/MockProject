package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.demo.model.CoursePostDto;
import com.example.demo.model.CoursePutDto;
import com.example.demo.model.CustomResponse;
import com.example.demo.service.CourseService;
import com.example.demo.service.RestTemplateCallService;

@Service
public class CourseImpl implements CourseService{

	//cal service restemplate
	@Autowired
	private RestTemplateCallService restTemplateCallService;

	//call message from env
	@Autowired
	private Environment env;

	@Override
	public CustomResponse createCourse(CoursePostDto coursePostDto, String token) {
		return restTemplateCallService.postResponseWithToken(coursePostDto,
				env.getProperty("url.localhost")+ env.getProperty("url.course.create")
				,token);
	}

	@Override
	public CustomResponse getListCourse(String token, int page, int size) {
		return restTemplateCallService.getResponseWithToken(
				env.getProperty("url.localhost")+ env.getProperty("url.course.listPagination")+"?pageSize="+size+"&pageNo="+page,token);
	}

	@Override
	public CustomResponse findCourseByid(Integer idCourse) {
		return restTemplateCallService.getResponse(
				env.getProperty("url.localhost")+ env.getProperty("url.course.findCourse")+"?id="+idCourse);
	}

	@Override
	public CustomResponse updateCourse(CoursePutDto coursePutDto, String token, Integer idCourse){
		return restTemplateCallService.PutResponseWithToken(coursePutDto,
				env.getProperty("url.localhost")+ env.getProperty("url.course.updateCourse")+"?id="+idCourse
				,token);
	}

	@Override
	public CustomResponse getListCourseByIdCategory(String idCategory) {
		return restTemplateCallService.getResponse(
				env.getProperty("url.localhost")+ env.getProperty("url.course.listByIdCategory")+"?id="+idCategory
				);
	}

	@Override
	public CustomResponse getListCourseByidPostWithStatusIsTrue(Integer idPost) {
		return restTemplateCallService.getResponse(
				env.getProperty("url.localhost")+ env.getProperty("url.course.listCourseByIdPost")+"?id="+idPost
				);
	}

	@Override
	public CustomResponse findAll(String token, String urlRequestParam) {
		return restTemplateCallService.getResponseWithToken(env.getProperty("url.localhost")+ env.getProperty("url.course.findAll")+urlRequestParam, token);
	}
}
