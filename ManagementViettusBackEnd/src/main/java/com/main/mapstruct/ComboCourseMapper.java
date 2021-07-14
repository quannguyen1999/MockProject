package com.main.mapstruct;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import com.main.entity.ComboCourse;
import com.main.entity.Course;
import com.main.model.ComboCourseGetDto;
import com.main.model.CourseGetDto;


@Mapper(
        componentModel = "spring"
)
public interface ComboCourseMapper {
	
	List<ComboCourseGetDto> listComboCourseToComboCourseGetDto(List<ComboCourse> listComboCourses);
	
	default ComboCourseGetDto ComboCourseToComboCourseGetDto(ComboCourse comboCourse) {
		if(comboCourse == null) {
			return null;
		}
		ComboCourseGetDto comboCourseGetDto = new ComboCourseGetDto();
		comboCourseGetDto.setId(comboCourse.getId());
		comboCourseGetDto.setNameCourse(comboCourse.getNameCourse());
		comboCourseGetDto.setTotalPrice(comboCourse.getTotalPrice());
		comboCourseGetDto.setListCourseGetDtos(new ArrayList<CourseGetDto>());
		if(comboCourse != null && comboCourse.getListComboCourse().size() >= 1) {
			for(Course course:comboCourse.getListComboCourse()) {
			        CourseGetDto courseGetDto = new CourseGetDto();
			        courseGetDto.setUserName( course.getAccount() != null ? course.getAccount().getUserName() : null);
			        courseGetDto.setNamePost( course.getPost() != null ?  course.getPost().getName() : null);
			        courseGetDto.setIdPost(course.getPost() != null ?  course.getPost().getIdPost() : null );
			        courseGetDto.setContent( course.getContent() );
			        courseGetDto.setCreatedAt( course.getCreatedAt() );
			        courseGetDto.setIdCourse( course.getIdCourse() );
			        courseGetDto.setImage( course.getImage() );
			        courseGetDto.setLinkCourse( course.getLinkCourse() );
			        courseGetDto.setPrice( course.getPrice() );
			        courseGetDto.setStatus( course.getStatus() );
			        courseGetDto.setTitle( course.getTitle() );
			        courseGetDto.setTypeLearn( course.getTypeLearn() );
			        comboCourseGetDto.getListCourseGetDtos().add(courseGetDto);
			}
		}
		return comboCourseGetDto;
	}
	
	
}
