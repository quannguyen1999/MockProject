package com.main.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.main.model.ErrorMessage;
import com.main.model.RegistrationGetDto;
import com.main.model.RegistrationPostDto;
import com.main.model.RegistrationPutDto;
import com.main.service.RegistrationService;
import com.main.service.ResponseService;
import com.main.service.ValidatorService;

@Component
public class RegistrationValidator {

	private static final String FIELD_ID = "id";
	
	@Autowired
	private ValidatorService validatorService;
	
	@Autowired
	private CourseValidator courseValidator;
	
	@Autowired
	private ResponseService responseService;
	
	@Autowired
	private RegistrationService registrationService;

	public List<ErrorMessage> validateRegisterCourse(RegistrationPostDto registrationPostDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = validatorService.checkBindingResult(result);
		if(listErrorResponses.size()>=1) {
			return listErrorResponses;
		}
		for(int idCourse :registrationPostDto.getListComboCourse()) {
			listErrorResponses =  courseValidator.validateIdCourse(String.valueOf(idCourse));
			if(listErrorResponses.size() >= 1) {
				break;
			}
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateUpdateRegistration(RegistrationPutDto registrationPutDto,String id) {
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		Integer idConverter = 0;
		try {
			idConverter = Integer.parseInt(id);
		} catch (Exception e) {
			listErrorResponses.add(new ErrorMessage(FIELD_ID,
					responseService.getMessage("registration.id.invalid")));
			return listErrorResponses;
		}
		RegistrationGetDto registrationGetDto = registrationService.findRegistrationById(idConverter);
		if(registrationGetDto == null) {
			listErrorResponses.add(new ErrorMessage(FIELD_ID,
					responseService.getMessage("registration.id.notFound")));
			return listErrorResponses;
		}else if(registrationGetDto.getStatus() == true) {
			listErrorResponses.add(new ErrorMessage(FIELD_ID,
					responseService.getMessage("registration.status.isTrue")));
			return listErrorResponses;
		}
		return listErrorResponses;
	}
}
