package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.demo.model.ContactPostDto;
import com.example.demo.model.CustomResponse;
import com.example.demo.service.ContactService;
import com.example.demo.service.RestTemplateCallService;

@Service
public class ContactImpl implements ContactService {

	//cal service restemplate
	@Autowired
	private RestTemplateCallService restTemplateCallService;
	
	//call url from file env
	@Autowired
	private Environment env;

	@Override
	public CustomResponse getListContactByEmail(String token, String email, int page, int size) {
		return restTemplateCallService
				.getResponseWithToken(env.getProperty("url.localhost") + env.getProperty("url.contact.listEmail")
				+ "?email=" + email+ "?pageSize=" + size + "&pageNo=" + page , token);
	}

	@Override
	public CustomResponse createContact(ContactPostDto contactPostDto) {
		return restTemplateCallService.postResponse(contactPostDto,
				env.getProperty("url.localhost") + env.getProperty("url.contact.create"));
	}

	@Override
	public CustomResponse getListContact(String token, int page, int size) {
		return restTemplateCallService.getResponseWithToken(env.getProperty("url.localhost")
				+ env.getProperty("url.contact.listPagination") + "?pageSize=" + size + "&pageNo=" + page, token);
	}
}
