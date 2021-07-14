package com.example.demo.service;

import java.util.List;

import com.example.demo.model.CustomResponse;
import com.example.demo.model.RegistrationGetDto;
import com.example.demo.model.RegistrationPostDto;

public interface RegistrationService {
	//
	CustomResponse register(RegistrationPostDto registrationPostDto);

	//Page<RegistrationGetDto>
	CustomResponse getListRegistration(String token, int page, int size);
	
	//List<RegistrationGetDto>
	CustomResponse getListRegistrationByidCourse(Integer idCourse);

	//Page<RegistrationGetDto> version 2
	CustomResponse findAll(String token, String urlRequestParam);
}