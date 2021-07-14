package com.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import com.main.entity.QReport;
import com.main.entity.Report;

public interface ReportRepository   extends JpaRepository<Report, Integer>,
QuerydslPredicateExecutor<Report>,
QuerydslBinderCustomizer<QReport>{
	@Override
	default void customize(QuerydslBindings bindings, QReport root) {
	}
	
	Report findTop1ByOrderByCreatedDateDesc();
	
//	createdDate
}
