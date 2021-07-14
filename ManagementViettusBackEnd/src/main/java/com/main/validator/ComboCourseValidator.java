package com.main.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.main.model.CourseComboPutDto;
import com.main.model.ErrorMessage;
import com.main.service.ComboCourseService;
import com.main.service.ResponseService;
import com.main.service.ValidatorService;

@Component
public class ComboCourseValidator {
	@Autowired
	private ValidatorService validatorService;

	@Autowired
	private ComboCourseService comboCourseService;

	@Autowired
	private PostValidator postValidator;

	@Autowired
	private ResponseService responseService;
	
	private static String FIELD_ID = "id";
	
	public List<ErrorMessage> validateListComboByIdPostCourse(String idPost) {
		List<ErrorMessage> listErrorResponses = postValidator.validateIdPost(idPost);
		if(listErrorResponses != null && listErrorResponses.size() >= 1) {
			return listErrorResponses;
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateIdCourse(String idCourse) {
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		if(idCourse == null || idCourse.isEmpty() == true) {
			listErrorResponses.add(new ErrorMessage(FIELD_ID,
					responseService.getMessage("comboCourse.id.isRequired")));
			return listErrorResponses;
		}
		if(comboCourseService.findById(idCourse) == null){
			listErrorResponses.add(new ErrorMessage(FIELD_ID,
					responseService.getMessage("comboCourse.id.notFound")));
		};
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateUpdateComboPost(CourseComboPutDto courseComboPutDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = validatorService.checkBindingResult(result);
		if(listErrorResponses.size()>=1) {
			return listErrorResponses;
		}
		if(comboCourseService.findById(courseComboPutDto.getId()) == null){
			listErrorResponses.add(new ErrorMessage(FIELD_ID,
					responseService.getMessage("comboCourse.id.notFound")));
		};
		return listErrorResponses;
	}
}
