package com.h4d1.rule.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.h4d1.rule.entity.Rule;

public interface RuleRepository extends JpaRepository<Rule, String> {
	@Query("select r from Rule r where r.isActive = true")
	Rule findByIsDefaultTrue();
}
