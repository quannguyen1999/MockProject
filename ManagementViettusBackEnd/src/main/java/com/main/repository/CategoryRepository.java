package com.main.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.main.entity.Category;
import com.main.entity.QCategory;
import com.main.entity.TypeCategory;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Integer>,
											QuerydslPredicateExecutor<Category>,
											QuerydslBinderCustomizer<QCategory>{
	@Override
	default void customize(QuerydslBindings bindings, QCategory root) {
		bindings.bind(root.name).first((path, value)->path.containsIgnoreCase(value.replaceAll("-", " ")));
	}
	
	List<Category> findByName(String name);

	//find all category not in that id
	List<Category> findByIdCategoryNotIn(Collection<Integer> id);

	//find all category by status is true
	List<Category>	findByStatusTrue();

	//find all category with status true and typeCategory in
	List<Category> findByStatusTrueAndTypeCategoryIn(List<TypeCategory> typeCategory);
}
