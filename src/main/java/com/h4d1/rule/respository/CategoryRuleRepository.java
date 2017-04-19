package com.h4d1.rule.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.h4d1.rule.entity.CategoryRule;

public interface CategoryRuleRepository extends JpaRepository<CategoryRule, String> {
	@Query("select cr from CategoryRule cr where cr.viewpointRule.id = :viewpointRuleId")
	List<CategoryRule> findByViewpointRuleId(@Param("viewpointRuleId") String viewpointRuleId);
}
