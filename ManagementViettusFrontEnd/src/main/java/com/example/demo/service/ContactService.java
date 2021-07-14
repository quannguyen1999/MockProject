package com.example.demo.service;

import com.example.demo.model.ContactPostDto;
import com.example.demo.model.CustomResponse;

public interface ContactService {
	public CustomResponse createContact(ContactPostDto contactPostDto);

	// Page<ContactGetDto>
	public CustomResponse getListContact(String token, int page, int size);
	// Page<ContactGetDto>
	public CustomResponse getListContactByEmail(String token, String email,  int page, int size);
}
