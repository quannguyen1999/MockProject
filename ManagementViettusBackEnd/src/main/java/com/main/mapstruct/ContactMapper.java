package com.main.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.main.entity.Contact;
import com.main.model.ContactGetDto;
import com.main.model.ContactPostDto;

@Mapper(componentModel = "spring")
public interface ContactMapper {
	@Mapping(target = "dateCreated", ignore = true)
	@Mapping(target = "idContact", ignore = true)
	Contact contactPostDtoToContact(ContactPostDto contactPostDto);

	ContactPostDto contactToContactPostDto(Contact contact);

	Contact contactGetDtoToContact(ContactGetDto contactGetDto);

	ContactGetDto contactToContactGetDto(Contact contact);

	List<ContactGetDto> listContactToListContactGetDto(List<Contact> listconContacts);

}
