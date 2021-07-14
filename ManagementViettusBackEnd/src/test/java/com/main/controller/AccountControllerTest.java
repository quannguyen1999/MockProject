package com.main.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.main.repository.AccountRepository;
import com.main.security.JwtTokenFilter;
import com.main.service.AccountService;
import com.main.service.JwtTokenService;
import com.main.service.ResponseService;
import com.main.validator.AccountValidator;

@SpringBootTest
class AccountControllerTest {
	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

//
//	@Test
//	void testGetTypeAccount() throws Exception {
//		this.mockMvc.perform(MockMvcRequestBuilders.get(AccountController.API_ACCOUNT+"/listV2"))
//		.andExpect(status().isOk());
//	}
}
