package com.main.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.main.entity.Account;
import com.main.model.AccountCreateDto;
import com.main.model.AccountGetDto;
import com.main.model.AccountPostDto;
import com.main.model.AccountUpdateDto;
import com.querydsl.core.types.Predicate;


public interface AccountService {
	//use when create by admin
	Boolean insert(AccountPostDto accountPostDto);
	
	//when use in signUp
	Boolean create(AccountCreateDto accountCreateDto);

	//list account
	List<AccountGetDto> list();
	
	//list Account version 2
	PageImpl<AccountGetDto> findAll(Predicate predicate, Pageable pageable);
	
	//list account by pagination
	PageImpl<AccountGetDto> listPagination(Integer pageNo, Integer pageSize);

	//find account by username
	AccountGetDto findByUserName(String id);
	
	//find username
	Account findByUserNameAndReturnAccount(String id);

	//so sánh password và hash
	Boolean comparePassword(String passwordHash, String password);
	
	//delete account by username
	Boolean deleteByIUserName(String username);
	
	//update account by username
	AccountGetDto updateAccountByUsername(String username, AccountUpdateDto accountUpdateDto);
}
