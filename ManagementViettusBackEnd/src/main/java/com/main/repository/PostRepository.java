package com.main.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.main.entity.Post;
import com.main.entity.QPost;
import com.main.entity.TypeCategory;
import com.main.entity.TypePost;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>,
								QuerydslPredicateExecutor<Post>,
								QuerydslBinderCustomizer<QPost>{
	//find by category id and type post 
	List<Post> findByCategoryIdCategoryAndTypePost(Integer idCategory, TypePost typePost);

	//find by category and type post and status is true
	List<Post> findByCategoryIdCategoryAndCategoryStatusIsTrueAndTypePostAndStatusIsTrue(Integer idCategory, TypePost typePost);

	//find list post with top 4 
	//and by category id 
	//and typePost 
	//and status is true
	List<Post> findTop4ByCategoryIdCategoryAndTypePostAndStatusIsTrue(Integer idCategory, TypePost typePost);

	//find pagination post with status true
	//and by id Post self 
	//and typePost 
	//and by pageable
	Page<Post> findByPostIdPostAndTypePostAndStatusIsTrue(Integer idPost, TypePost typePost, Pageable pageable);

	//find pagination post with status true
	//by idCategory 
	//by type Post
	//by pageable
	Page<Post> findByCategoryIdCategoryAndTypePostAndStatusIsTrue(Integer idCategory, TypePost typePost, Pageable pageable);

	//find pagination post 
	//by username
	//by pageable
	Page<Post> findByAccountUserName(String userName, Pageable pageable);

	//find post by idCategory and typePost and status is true
	Post findByIdPostAndTypePostAndStatusIsTrue(Integer idCategory, TypePost typePost);

	//find pagination post 
	//by category id
	//status is true
	//by TypeCategory 
	//by name post starting with character...
	Page<Post> findByCategoryIdCategoryAndStatusIsTrueAndCategoryTypeCategoryAndNameStartingWith(Integer idCategory,TypeCategory typeCategory,String name, Pageable pageable);
	
	//find by id Post with status is true 
	//by type category
	//and category must status is true
	Post findByIdPostAndStatusIsTrueAndCategoryTypeCategoryAndCategoryStatusIsTrue(Integer idPost, TypeCategory typeCategory);

	//find by id Post with status is true 
	//by type post 
	//by type category
	//and category must status is true
	Post findByIdPostAndStatusIsTrueAndTypePostAndCategoryTypeCategoryAndCategoryStatusIsTrue(Integer idPost,TypePost typePost,TypeCategory typeCategory);

	//list all post type post menu and typecategory is course with status category is true
	List<Post> findByTypePostAndCategoryTypeCategoryAndCategoryStatusIsTrue(TypePost typePost, TypeCategory tyeCategory);

	//findAll version 2
	//this method is custom binding when have request to post findAll
	@Override
	default void customize(QuerydslBindings bindings, QPost root) {
		bindings.bind(root.name).first((path, value)->path.containsIgnoreCase(value.replaceAll("-"," ")));
		bindings.excluding(root.account.password);
		bindings.excluding(root.dateCreated);
		bindings.excluding(root.dateUpdated);
	}
	
	Page<Post> findByNameContainingAndStatusIsTrue(String name, Pageable pageable);
}