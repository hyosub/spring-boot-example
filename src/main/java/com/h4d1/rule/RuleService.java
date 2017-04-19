package com.h4d1.rule;

import java.util.List;

import com.h4d1.exception.ServiceException;
import com.h4d1.rule.entity.CategoryRule;
import com.h4d1.rule.entity.Rule;
import com.h4d1.rule.entity.ValueRule;
import com.h4d1.rule.entity.ViewpointRule;

public interface RuleService {
	void createRule(Rule rule) throws ServiceException;
	Rule createRule(String id, Rule rule) throws ServiceException;
	Rule getRule(String id) throws ServiceException;
	Rule getActiveRule() throws ServiceException;
	List<Rule> getRules() throws ServiceException;
	ViewpointRule getViewpointRule(String viewpointRuleId) throws ServiceException;
	List<ViewpointRule> getViewpointRules(String ruleId) throws ServiceException;
	CategoryRule getCategoryRule(String categoryRuleId) throws ServiceException;
	List<CategoryRule> getCategoryRules(String viewpointRuleId) throws ServiceException;
	ValueRule getValueRule(String valueRuleId) throws ServiceException;
	Rule activateRule(String ruleId) throws ServiceException;
	Rule modifyRule(String id, Rule rule) throws ServiceException;
}
