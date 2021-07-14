package com.main.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.main.entity.Course;
import com.main.mapstruct.CourseMapper;
import com.main.model.CourseGetDto;
import com.main.model.CoursePostDto;
import com.main.model.CoursePutDto;
import com.main.repository.AccountRepository;
import com.main.repository.CourseRepository;
import com.main.repository.PostRepository;
import com.main.service.CourseService;
import com.querydsl.core.types.Predicate;

@Service
public class CourseImpl implements CourseService{

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CourseMapper courseMapper;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Boolean createCourse(CoursePostDto coursePostDto) {
		try {
			Course course = courseMapper.coursePostDtoToCourse(coursePostDto);
			course.setStatus(true);
			course.setCreatedAt(LocalDate.now());
			course.setPost(postRepository.findById(coursePostDto.getIdPost()).get());
			course.setAccount(accountRepository.findById(coursePostDto.getUserName()).get());
			courseRepository.save(course);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return false;
		}
		return true;
	}

	@Override
	public PageImpl<CourseGetDto> listPagination(Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Course> page =  courseRepository.findAll(paging);
		return new PageImpl<CourseGetDto>(
				page.getContent().stream().map(course->
				new CourseGetDto(course.getIdCourse(),
						course.getTitle(),
						course.getAccount().getUserName(),
						course.getContent(),
						course.getImage(),
						course.getTypeLearn(),
						course.getPrice(),
						course.getLinkCourse(),
						course.getStatus(),
						course.getCreatedAt(),
						course.getPost().getIdPost(),
						course.getPost().getName())).collect(Collectors.toList()),
				paging, page.getTotalElements()
				);
	}

	@Override
	public CourseGetDto findCourseById(int id) {
		Optional<Course> cateOptional = courseRepository.findById(id);
		return cateOptional.isPresent() ? courseMapper.courseToCourseGetDto(cateOptional.get()) : null;
	}

	@Override
	public Boolean deleteCourseById(int id) {
		try {
			courseRepository.deleteById(id);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public CourseGetDto updateCourse(CoursePutDto coursePutDto, Integer idCourse) {
		Course courseCurrent = courseRepository.findById(idCourse).get();
		if(coursePutDto.getTypeLearn() != null) {
			courseCurrent.setTypeLearn(coursePutDto.getTypeLearn());
		}
		if(coursePutDto.getContent() != null) {
			courseCurrent.setContent(coursePutDto.getContent());
		}
		if(coursePutDto.getImage() != null) {
			courseCurrent.setImage(coursePutDto.getImage());
		}
		if(coursePutDto.getLinkCourse() != null) {
			courseCurrent.setLinkCourse(coursePutDto.getLinkCourse());
		}
		if(coursePutDto.getTitle() != null) {
			courseCurrent.setTitle(coursePutDto.getTitle());
		}
		if(coursePutDto.getStatus() != null) {
			courseCurrent.setStatus(coursePutDto.getStatus());
		}
		courseRepository.save(courseCurrent);
		return courseMapper.courseToCourseGetDto(courseCurrent);
	}

	@Override
	public List<CourseGetDto> listAllCourseByIdCategory(Integer idCategory) {
		return courseMapper.listCourseToListCourseGetDto(courseRepository.findByPostCategoryIdCategory(idCategory));
	}

	@Override
	public PageImpl<CourseGetDto> listPaginationByidCategoryForClient(Integer idCategory, Integer pageNo,
			Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Course> page =  courseRepository.findByPostCategoryIdCategoryAndPostCategoryStatusIsTrueAndPostStatusIsTrue(idCategory, paging);
		return new PageImpl<CourseGetDto>(
				page.getContent().stream().map(course->
				new CourseGetDto(course.getIdCourse(),
						course.getTitle(),
						course.getAccount().getUserName(),
						course.getContent(),
						course.getImage(),
						course.getTypeLearn(),
						course.getPrice(),
						course.getLinkCourse(),
						course.getStatus(),
						course.getCreatedAt(),
						course.getPost().getIdPost(),
						course.getPost().getName())).collect(Collectors.toList()),
				paging, page.getTotalElements()
				);
	}

	@Override
	public List<CourseGetDto> listAllCourseByIdPostMenu(Integer idPost) {
		return courseMapper.listCourseToListCourseGetDto(courseRepository.findByPostIdPostAndStatusIsTrue(idPost));
	}

	@Override
	public PageImpl<CourseGetDto> findAll(Predicate predicate, Pageable pageable) {
		Page<Course> page = courseRepository.findAll(predicate, pageable);
		return new PageImpl<CourseGetDto>(
				page.getContent().stream().map(course->
				new CourseGetDto(course.getIdCourse(),
						course.getTitle(),
						course.getAccount().getUserName(),
						course.getContent(),
						course.getImage(),
						course.getTypeLearn(),
						course.getPrice(),
						course.getLinkCourse(),
						course.getStatus(),
						course.getCreatedAt(),
						course.getPost().getIdPost(),
						course.getPost().getName())).collect(Collectors.toList()),
				pageable, page.getTotalElements()
				);
	}





















}
