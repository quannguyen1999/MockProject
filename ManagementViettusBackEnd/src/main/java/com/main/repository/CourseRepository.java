package com.main.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.main.entity.Course;
import com.main.entity.QCourse;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>,
QuerydslPredicateExecutor<Course>,
QuerydslBinderCustomizer<QCourse>{
	//find all course by id category
	List<Course> findByPostCategoryIdCategory(Integer idCategory);

	List<Course> findByPostIdPostAndStatusIsTrue(Integer idPost);

	//find all course by id category with status is true and post status also is true
	Page<Course> findByPostCategoryIdCategoryAndPostCategoryStatusIsTrueAndPostStatusIsTrue(Integer idCategory, Pageable pageable);
	
	@Override
	default void customize(QuerydslBindings bindings, QCourse root) {
		bindings.bind(root.title).first((path, value)->path.containsIgnoreCase(value.replaceAll("-", " ")));
		bindings.bind(root.price)
			.all((path, value)->{
				  List<? extends Float> prices = new ArrayList<>(value);
                  if (prices.size() == 1) {
                      return Optional.of(path.eq(prices.get(0)));
                  } else {
                      Float from = prices.get(0);
                      Float to = prices.get(1);
                      return Optional.of(path.between(from, to));
                  }
			});
		
		bindings.excluding(root.content);
	}
}
