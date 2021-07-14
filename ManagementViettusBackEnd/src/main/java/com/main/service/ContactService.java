package com.main.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.main.model.ContactGetDto;
import com.main.model.ContactPostDto;

public interface ContactService {
	//find all
	List<ContactGetDto> findAll();

	//list pagination by phone
	PageImpl<ContactGetDto> listPaginationWithPhone(Integer pageNo, Integer pageSize, String phone);

	//list pagination by email
	PageImpl<ContactGetDto> listPaginationWithEmail(Integer pageNo, Integer pageSize, String email);

	//list pagination
	PageImpl<ContactGetDto> listPagination(Integer pageNo, Integer pageSize);

	//save contact
	Boolean saveContact(ContactPostDto contactPostDto);

	//delete contact by id
	Boolean deleteContactById(int id);
	
	//find contact by id
	ContactGetDto findContactById(int id);
}
