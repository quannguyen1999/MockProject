package com.main.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.main.entity.Account;
import com.main.model.AccountCreateDto;
import com.main.model.AccountGetDto;
import com.main.model.AccountPostDto;

@Mapper(
		componentModel = "spring"
		)
public interface AccountMapper {
	AccountPostDto accountToAccountPostDto(Account account);

	AccountCreateDto accountTAccountCreateDto(Account account);

	Account accountPostToDToAccount(AccountPostDto accountPostDto);

	@Mapping(target = "password", ignore = true)
	Account accountCreateDtoToAccount(AccountCreateDto accountCreateDto);

	AccountGetDto accountToAccountGetDto(Account account);

	@Mapping(target = "password", ignore = true)
	Account accountGetDtoToAccount(AccountGetDto accountGetDto);

	List<AccountGetDto> listAccountToListAccountGetDto(List<Account> listAccounts);
}
