package com.main.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.main.entity.Post;
import com.main.entity.TypeCategory;
import com.main.model.CourseGetDto;
import com.main.model.CoursePostDto;
import com.main.model.CoursePutDto;
import com.main.model.ErrorMessage;
import com.main.service.CourseService;
import com.main.service.PostService;
import com.main.service.ResponseService;
import com.main.service.ValidatorService;

@Component
public class CourseValidator {
	@Autowired
	private ValidatorService validatorService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private CourseService courseService;

	@Autowired
	private ResponseService responseService;
	
	private static final String ID_COURSE = "idCourse";

	public List<ErrorMessage> validateCreateCourse(CoursePostDto coursePostDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = validatorService.checkBindingResult(result);
		if(listErrorResponses.size()>=1) {
			return listErrorResponses;
		}
		Post post = postService.findPostByIdAndReturnPost(coursePostDto.getIdPost());
		if(coursePostDto.getIdPost() == 0 || post == null) {
			
			listErrorResponses.add(new ErrorMessage(ID_COURSE,
					responseService.getMessage("post.id.notFound")));
			return listErrorResponses;
		}else if(post.getCategory().getTypeCategory().toString().equalsIgnoreCase(TypeCategory.COURSE.toString()) == false) {
			listErrorResponses.add(new ErrorMessage(ID_COURSE,
					responseService.getMessage("course.post.category.typeCategory.invalid")));
			return listErrorResponses;
		}else if(post.getStatus() == false) {
			listErrorResponses.add(new ErrorMessage(ID_COURSE,
					responseService.getMessage("course.post.status.isOff")));
			return listErrorResponses;
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateIdCourse(String idCourse) {
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		int idCourseConverter = 0;
		try {
			idCourseConverter=Integer.parseInt(idCourse);
		} catch (Exception e) {
				listErrorResponses.add(new ErrorMessage(ID_COURSE,
						responseService.getMessage("course.id.invalid")));
				return listErrorResponses;
		}
		CourseGetDto course = courseService.findCourseById(idCourseConverter);;//.findPostByIdTypeMenuAndCategoryIsCourse(idCourseConverter);
		if(course == null) {
			listErrorResponses.add(new ErrorMessage(ID_COURSE,
					responseService.getMessage("course.id.notFound")));
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateUpdateCourse(String idCourse, CoursePutDto coursePutDto) {
		List<ErrorMessage> listErrorResponses = validateIdCourse(idCourse);
		if(listErrorResponses.size() >= 1) {
			return listErrorResponses;
		}
		return listErrorResponses;
	}
}
