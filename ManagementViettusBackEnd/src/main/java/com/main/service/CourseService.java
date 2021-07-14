package com.main.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.main.model.CourseGetDto;
import com.main.model.CoursePostDto;
import com.main.model.CoursePutDto;
import com.querydsl.core.types.Predicate;

public interface CourseService {
	public Boolean createCourse(CoursePostDto coursePostDto);

	PageImpl<CourseGetDto> listPagination(Integer pageNo, Integer pageSize);

	PageImpl<CourseGetDto> listPaginationByidCategoryForClient(Integer idCategory, Integer pageNo, Integer pageSize);

	List<CourseGetDto> listAllCourseByIdCategory(Integer idCategory);

	List<CourseGetDto> listAllCourseByIdPostMenu(Integer idPost);

	public CourseGetDto findCourseById(int id);

	public Boolean deleteCourseById(int id);

	public CourseGetDto updateCourse(CoursePutDto coursePutDto, Integer idCourse);

	//list Course version 2
	PageImpl<CourseGetDto> findAll(Predicate predicate, Pageable pageable);
}