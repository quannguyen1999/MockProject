package com.example.demo.service;


import com.example.demo.model.CustomResponse;

public interface ComboCourseService {
	CustomResponse listComboCoursesByIdPostCourse(int idPost);
	
	//Page<AccountGetDto> version 2
	CustomResponse findAll(String token, String urlRequestParam);
}