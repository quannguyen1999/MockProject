package com.example.demo.service;

import com.example.demo.model.CoursePostDto;
import com.example.demo.model.CoursePutDto;
import com.example.demo.model.CustomResponse;

public interface CourseService {
	public CustomResponse createCourse(CoursePostDto coursePostDto, String token);

	public CustomResponse updateCourse(CoursePutDto coursePutDto, String token, Integer idCourse);

	//Page<CourseGetDto>
	public CustomResponse getListCourse(String token, int page, int size);

	public CustomResponse findCourseByid(Integer idCourse);

	//List<CourseGetDto>
	public CustomResponse getListCourseByIdCategory(String idCategory);

	//List<CourseGetDto>
	public CustomResponse getListCourseByidPostWithStatusIsTrue(Integer idPost);

	//Page<CourseGetDto> version 2
	CustomResponse findAll(String token, String urlRequestParam);
}
