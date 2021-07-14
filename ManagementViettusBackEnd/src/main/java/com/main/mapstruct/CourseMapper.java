package com.main.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.main.entity.Course;
import com.main.model.CourseGetDto;
import com.main.model.CoursePostDto;
import com.main.model.CoursePutDto;


@Mapper(
		componentModel = "spring"
		)
public interface CourseMapper {
	@Mappings({
		@Mapping(target = "userName", source = "account.userName"),
	})
	@Mapping(target = "idPost", ignore = true)
	CoursePostDto courseToCoursePostDto(Course course);
	
	@Mapping(target = "idPost", ignore = true)
	CoursePutDto courseToCoursePutDto(Course course);

	@Mappings({
		@Mapping(target = "account.userName", source = "userName"),
		@Mapping(target = "post.idPost", source = "idPost"),
		@Mapping(target = "idCourse", ignore = true),
		@Mapping(target = "createdAt", ignore = true),
		@Mapping(target = "status", ignore = true)
	})
	Course coursePostDtoToCourse(CoursePostDto coursePostDto);

	@Mappings({
		@Mapping(target = "userName", source = "account.userName"),
		@Mapping(target = "namePost", source = "post.name"),
		@Mapping(target = "idPost", source = "post.idPost"),
	})
	CourseGetDto courseToCourseGetDto(Course course);

	List<CourseGetDto> listCourseToListCourseGetDto(List<Course> listCourses);
}
