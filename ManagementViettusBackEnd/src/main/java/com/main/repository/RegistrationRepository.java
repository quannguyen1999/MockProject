package com.main.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.main.entity.QRegistration;
import com.main.entity.Registration;

public interface RegistrationRepository  extends JpaRepository<Registration, Integer>,
QuerydslPredicateExecutor<Registration>,
QuerydslBinderCustomizer<QRegistration>{
	List<Registration> findBylistComboCourseIdCourseAndStatusIsTrue(Integer idCourse);

	@Override
	default void customize(QuerydslBindings bindings, QRegistration root) {
		//find between created date
		bindings.bind(root.createdDate)
		.all((path, value) -> {
			List<? extends Date> dates = new ArrayList<>(value);
			if (dates.size() == 1) {
				return Optional.of(path.eq(dates.get(0)));
			} else {
				Date from = dates.get(0);
				Date to = dates.get(1);
				return Optional.of(path.between(from, to));
			}
		});

		bindings.bind(root.nameCustomer).first((path, value)->path.containsIgnoreCase(value));
		bindings.bind(root.phone).first((path, value)->path.containsIgnoreCase(value));

		bindings.excluding(root.lastModifiedBy);
		bindings.excluding(root.createdBy);
		bindings.excluding(root.lastModifiedDate);
	}
}
