package com.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.main.entity.ComboCourse;
import com.main.entity.QComboCourse;

@Repository
public interface ComboCourseRepository extends JpaRepository<ComboCourse, String>,
QuerydslPredicateExecutor<ComboCourse>,
QuerydslBinderCustomizer<QComboCourse>{
	
	List<ComboCourse> findByListComboCoursePostIdPost(Integer idPost);
	
	@Override
	default void customize(QuerydslBindings bindings, QComboCourse root) {
	}
}
