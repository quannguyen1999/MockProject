package com.main.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.main.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
	Page<Contact> findByPhone(String phone, Pageable pageable);

	List<Contact> findAll();

	Page<Contact> findAll(Pageable pageable);

	Page<Contact> findByEmail(String email, Pageable pageable);

}
