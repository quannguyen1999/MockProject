package com.main.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.main.entity.Contact;
import com.main.mapstruct.ContactMapper;
import com.main.model.ContactGetDto;
import com.main.model.ContactPostDto;
import com.main.repository.ContactRepository;
import com.main.service.ContactService;

@Service
public class ContactImpl implements ContactService {
	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ContactMapper contactMapper;

	@Override
	public List<ContactGetDto> findAll() {
		// TODO Auto-generated method stub
		return contactMapper.listContactToListContactGetDto(contactRepository.findAll());
	}

	@Override
	public Boolean saveContact(ContactPostDto contactPostDto) {
		Contact contact = contactMapper.contactPostDtoToContact(contactPostDto);
		contact.setDateCreated(LocalDate.now());

		try {
			contactRepository.save(contact);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Boolean deleteContactById(int id) {
		try {
			contactRepository.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private PageImpl<ContactGetDto> returnPage(Page<Contact> page, Pageable paging) {
		return new PageImpl<ContactGetDto>(
				page.getContent().stream()
						.map(contact -> new ContactGetDto(contact.getIdContact(), contact.getTitle(), contact.getName(),
								contact.getEmail(), contact.getAddress(), contact.getPhone(), contact.getContent(),
								contact.getDateCreated()))
						.collect(Collectors.toList()),
				paging, page.getTotalElements());
	}

	@Override
	public PageImpl<ContactGetDto> listPaginationWithPhone(Integer pageNo, Integer pageSize, String phone) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Contact> page = contactRepository.findByPhone(phone, paging);
		return returnPage(page, paging);
	}

	@Override
	public PageImpl<ContactGetDto> listPaginationWithEmail(Integer pageNo, Integer pageSize, String email) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Contact> page = contactRepository.findByEmail(email, paging);
		return returnPage(page, paging);
	}

	@Override
	public PageImpl<ContactGetDto> listPagination(Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Contact> page = contactRepository.findAll(paging);
		return returnPage(page, paging);
	}

	@Override
	public ContactGetDto findContactById(int id) {
		Optional<Contact> contact = contactRepository.findById(id);
		return contact.isPresent() ? contactMapper.contactToContactGetDto(contact.get()) : null;
	}
}
