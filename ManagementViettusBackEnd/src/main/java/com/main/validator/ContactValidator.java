package com.main.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.main.model.ContactPostDto;
import com.main.model.ErrorMessage;
import com.main.service.ContactService;
import com.main.service.ResponseService;
import com.main.service.ValidatorService;

@Component
public class ContactValidator {

	private static final String FIELD_ID = "idContact";

//	private static final String FIELD_NAME = "name";
//
//	private static final String FIELD_EMAIL = "email";
//
//	private static final String FIELD_PHONE = "phone";
//
//	private static final String FIELD_TITLE = "title";
//
//	private static final String FIELD_ADDRESS = "address";
//
//	private static final String FIELD_CONTENT = "content";

	@Autowired
	private ValidatorService validatorService;

	@Autowired
	private ContactService contactService;

	@Autowired
	private ResponseService responseService;

	public List<ErrorMessage> validateContactPost(ContactPostDto contactPostDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = validatorService.checkBindingResult(result);
		if (listErrorResponses.size() >= 1) {
			return listErrorResponses;
		}

		return listErrorResponses;
	}

	public List<ErrorMessage> validateIdContact(String idContact) {
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		int idConvert = 0;
		try {
			idConvert = Integer.parseInt(idContact);
		} catch (Exception e) {
			listErrorResponses
					.add(new ErrorMessage(FIELD_ID, 
							responseService.getMessage("contact.id.invalid")));
			return listErrorResponses;
		}
		if (contactService.findContactById(idConvert) == null) {
			listErrorResponses.add(
					new ErrorMessage(FIELD_ID, 
							responseService.getMessage("contact.id.notFound")));
		}
		return listErrorResponses;
	}
}
