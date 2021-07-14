package com.main.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.main.model.RegistrationGetDto;
import com.main.model.RegistrationPostDto;
import com.main.model.RegistrationPutDto;
import com.querydsl.core.types.Predicate;

public interface RegistrationService {
	public Boolean register(RegistrationPostDto registrationPostDto);
	
	PageImpl<RegistrationGetDto> listPagination(Integer pageNo, Integer pageSize);
	
	RegistrationGetDto updateRegistration(RegistrationPutDto registrationPutDto, int idRegistration);
	
	RegistrationGetDto findRegistrationById(int idRegistration);
	
	List<RegistrationGetDto> listAllRegistrationByIdCourseAndStatusIsTrue(int idCourse);

	//list Registration version 2
	PageImpl<RegistrationGetDto> findAll(Predicate predicate, Pageable pageable);
}
