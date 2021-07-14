package com.main.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.main.entity.Account;
import com.main.entity.TypeAccount;
import com.main.mapstruct.AccountMapper;
import com.main.model.AccountCreateDto;
import com.main.model.AccountGetDto;
import com.main.model.AccountPostDto;
import com.main.model.AccountUpdateDto;
import com.main.repository.AccountRepository;
import com.main.service.AccountService;
import com.querydsl.core.types.Predicate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AccountImpl implements AccountService{
	//when create account by admin, password default is below
	private final String DEDAULT_PASSWORD = "Freetus@123";

	private final AccountRepository accountRepository;

	private final PasswordEncoder passwordEncoder;

	private final AccountMapper accountMapper;

	private String hashPassword(String password) {
		return passwordEncoder.encode(password);
	}

	@Override
	public Boolean comparePassword(String passwordHash, String password) {
		return passwordEncoder.matches(passwordHash, password);
	}

	@Override
	public Boolean insert(AccountPostDto accountPostDto)  {
		try {
			accountPostDto.setPassword(hashPassword(accountPostDto.getPassword()));
			Account account = accountMapper.accountPostToDToAccount(accountPostDto);
			accountRepository.save(account);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<AccountGetDto> list() {
		return accountMapper.listAccountToListAccountGetDto(accountRepository.findAll());
	}

	@Override
	public AccountGetDto findByUserName(String id) {
		Optional<Account> accountFind = accountRepository.findById(id);
		return accountFind.isPresent() ? accountMapper.accountToAccountGetDto(accountFind.get()) : null;
	}

	@Override
	public Account findByUserNameAndReturnAccount(String id) {
		Optional<Account> accountFind = accountRepository.findById(id);
		return accountFind.isPresent() ? accountFind.get() : null;
	}

	@Override
	public Boolean create(AccountCreateDto accountCreateDto) {
		try {
			Account account = accountMapper.accountCreateDtoToAccount(accountCreateDto);
			account.setPassword(hashPassword(DEDAULT_PASSWORD));
			System.out.println(account);
			accountRepository.save(account);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Boolean deleteByIUserName(String username) {
		try {
			accountRepository.deleteById(username);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	@Override
	public AccountGetDto updateAccountByUsername(String username, AccountUpdateDto accountUpdateDto) {
		Account account = accountRepository.findById(username).get();
		account.setTypeAccount(accountUpdateDto.getTypeAccount().toString().equalsIgnoreCase(TypeAccount.ADMIN.toString()) ?
				TypeAccount.ADMIN : TypeAccount.COLLABORATOR);
		accountRepository.save(account);
		return accountMapper.accountToAccountGetDto(account);
	}

	@Override
	public PageImpl<AccountGetDto> listPagination(Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Account> page =  accountRepository.findAll(paging);
		return new PageImpl<AccountGetDto>(
				page.getContent().stream().map(account-> new AccountGetDto(account.getUserName(), account.getTypeAccount())).collect(Collectors.toList()),
				paging, page.getTotalElements()
				);
	}

	@Override
	public PageImpl<AccountGetDto> findAll(Predicate predicate, Pageable pageable) {
		Page<Account> page = accountRepository.findAll(predicate, pageable);
		return new PageImpl<AccountGetDto>(
				page.getContent().stream().map(account-> new AccountGetDto(account.getUserName(), account.getTypeAccount())).collect(Collectors.toList()),
				pageable, page.getTotalElements()
				);
	}

}
