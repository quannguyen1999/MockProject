package com.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.main.entity.Account;
import com.main.entity.QAccount;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>, 
									QuerydslPredicateExecutor<Account>,
									QuerydslBinderCustomizer<QAccount>{
	@Override
	default void customize(QuerydslBindings bindings, QAccount root) {
		bindings.bind(root.userName).first((path, value)->path.containsIgnoreCase(value));
		bindings.excluding(root.password);
	}
}
