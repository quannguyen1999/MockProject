package com.main.mapstruct;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.main.entity.Course;
import com.main.entity.Registration;
import com.main.model.RegistrationGetDto;
import com.main.model.RegistrationPostDto;

@Mapper(
		componentModel = "spring"
		)
public interface RegistrationMapper {
	@Mappings({
		@Mapping(target = "listComboCourse", source = "listComboCourse"),
		@Mapping(target = "URLFacebook", source = "urlFacebook"),
		@Mapping(target = "createdBy", ignore = true),
		@Mapping(target = "createdDate", ignore = true),
		@Mapping(target = "lastModifiedBy", ignore = true),
		@Mapping(target = "lastModifiedDate", ignore = true),
		@Mapping(target = "comboCourse", ignore = true),
		@Mapping(target = "idRegistration", ignore = true),
		@Mapping(target = "status", ignore = true),
		@Mapping(target = "typeRegistration", ignore = true)
	})
	abstract Registration registrationPostDtoToRegistration(RegistrationPostDto registrationPostDto);
	
	default List<Course> mapListIdCourseToListComboCourse(List<Integer> listIdCourse) {
		List<Course> listCourses = new ArrayList<Course>();
		listIdCourse.forEach(t->{
			listCourses.add(new Course(t));
		});
		return listCourses;
	}
	
	@Mappings({
		@Mapping(target = "listComboCourse", source = "listComboCourse"),
		@Mapping(target = "urlFacebook", ignore = true)
	})
	RegistrationPostDto registrationToregistrationPostDto(Registration registration);
	
	default List<Integer> mapListComboCourseToListIdCourse(List<Course> listCourse) {
		List<Integer> listIdCourses = new ArrayList<Integer>();
		listCourse.forEach(t->{
			listIdCourses.add(t.getIdCourse());
		});
		return listIdCourses;
	}

	@Mappings({
		@Mapping(target = "listComboCourse", source = "listComboCourse"),
		@Mapping(target = "dateCreated", source = "createdDate"),
		@Mapping(target = "nameCourse", ignore = true)
	})
	RegistrationGetDto registrationToRegistrationGetDto(Registration registration);

	@Mappings({
		@Mapping(target = "titleCourse", source = "course.title"),
		@Mapping(target = "listComboCourse", source = "listComboCourse")
	})
	List<RegistrationGetDto> listRegistrationToListRegistrationGetDto(List<Registration> listRegistrations);
}
