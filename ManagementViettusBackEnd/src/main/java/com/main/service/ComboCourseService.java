package com.main.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.main.model.ComboCourseGetDto;
import com.main.model.CourseComboPutDto;
import com.querydsl.core.types.Predicate;

public interface ComboCourseService {
	//list combo course by id post course
	List<ComboCourseGetDto> listComboCoursesByIdPostCourse(String idPost);
	
	//find all and query
	PageImpl<ComboCourseGetDto> findAll(Predicate predicate, Pageable pageable);
	
	//find combo course by id
	ComboCourseGetDto findById(String id);
	
	//update combo course
	ComboCourseGetDto updateComboCourse(CourseComboPutDto courseComboPutDto);
}