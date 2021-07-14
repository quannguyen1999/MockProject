package com.main.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.main.entity.ComboCourse;
import com.main.entity.Course;
import com.main.entity.Registration;
import com.main.entity.TypeRegistration;
import com.main.mapstruct.RegistrationMapper;
import com.main.model.RegistrationGetDto;
import com.main.model.RegistrationPostDto;
import com.main.model.RegistrationPutDto;
import com.main.repository.ComboCourseRepository;
import com.main.repository.CourseRepository;
import com.main.repository.RegistrationRepository;
import com.main.service.RegistrationService;
import com.querydsl.core.types.Predicate;

@Service
public class RegistrationImpl implements RegistrationService{

	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private RegistrationMapper registrationMapper;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private ComboCourseRepository comboCourseRepository;
	
	@Transactional
	@Override
	public Boolean register(RegistrationPostDto registrationPostDto) {
		try {
			StringBuilder idComboCourse = new StringBuilder();
			Float totalPrice = 0f;
			Registration registration = registrationMapper.registrationPostDtoToRegistration(registrationPostDto);
			Set<Course> listCourses = new HashSet<Course>();
			for(Course course :registration.getListComboCourse()) {
				listCourses.add(courseRepository.findById(course.getIdCourse()).get());
				idComboCourse.append("-"+course.getIdCourse());
				totalPrice = totalPrice + course.getPrice();
			}
			if(listCourses.size() > 1) {
				ComboCourse comboCourse = null;
				Optional<ComboCourse> optional = comboCourseRepository.findById(idComboCourse.toString());
				if(optional.isPresent() == true) {
					comboCourse = optional.get();
				}else {
					comboCourse = new ComboCourse(idComboCourse.toString(),null, Lists.newArrayList(listCourses),totalPrice);
					comboCourseRepository.save(comboCourse);
				}
				registration.setComboCourse(comboCourse);
				registration.setTypeRegistration(TypeRegistration.COMBO);
				registration.setListComboCourse(null);
			}else {
				registration.setListComboCourse(Lists.newArrayList(listCourses));
			}
			registrationRepository.save(registration);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true; 
	}

	@Override
	public PageImpl<RegistrationGetDto> listPagination(Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Registration> page =  registrationRepository.findAll(paging);
		return returnPage(page, paging);
	}
	
	private PageImpl<RegistrationGetDto> returnPage(Page<Registration> page,Pageable paging){
		return new PageImpl<RegistrationGetDto>(
				page.getContent().stream().map(registration-> new RegistrationGetDto(
						registration.getIdRegistration(),
						registration.getTypeRegistration(),
						registration.getStatus(),
						registration.getNameCustomer(),
						registration.getPhone(),
						registration.getEmail(),
						registration.getURLFacebook(),
						registration.getTypePay(),
						registration.getListComboCourse().stream().map(t->t.getIdCourse()).collect(Collectors.toList()),
						registration.getComboCourse(),
						registration.getCreatedDate(),
						registration.getListComboCourse() != null && registration.getListComboCourse().size() == 1 ? registration.getListComboCourse().get(0).getTitle() : registration.getComboCourse().getNameCourse())).collect(Collectors.toList()),
				paging, page.getTotalElements()
				);
	}

	@Override
	public RegistrationGetDto updateRegistration(RegistrationPutDto registrationPutDto, int idRegistration) {
		Registration registration = registrationRepository.findById(idRegistration).get();
		if(registrationPutDto != null) {
			registration.setStatus(registrationPutDto.getStatus());
		}
		if(registrationPutDto.getStatus() == false) {
			registrationRepository.deleteById(idRegistration);
		}else {
			registrationRepository.save(registration);
		}
		return registrationMapper.registrationToRegistrationGetDto(registration);
	}

	@Override
	public RegistrationGetDto findRegistrationById(int idRegistration) {
		Optional<Registration> reOptional = registrationRepository.findById(idRegistration);
		return reOptional.isPresent() ? registrationMapper.registrationToRegistrationGetDto(reOptional.get()) : null;
	}

	@Override
	public List<RegistrationGetDto> listAllRegistrationByIdCourseAndStatusIsTrue(int idCourse) {
		return registrationMapper.listRegistrationToListRegistrationGetDto(registrationRepository.findBylistComboCourseIdCourseAndStatusIsTrue(idCourse));
	}

	@Override
	public PageImpl<RegistrationGetDto> findAll(Predicate predicate, Pageable pageable) {
		Page<Registration> page = registrationRepository.findAll(predicate, pageable);
		return returnPage(page, pageable);
	}
}
