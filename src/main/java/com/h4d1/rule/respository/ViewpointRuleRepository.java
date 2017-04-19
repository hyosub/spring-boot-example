package com.h4d1.rule.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.h4d1.rule.entity.ViewpointRule;

public interface ViewpointRuleRepository extends JpaRepository<ViewpointRule, String> {
	@Query("select vr from ViewpointRule vr where vr.rule.id = :ruleId")
	List<ViewpointRule> findByRuleId(@Param("ruleId") String ruleId);
}
