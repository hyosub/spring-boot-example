package com.h4d1.util.validation;

import java.util.HashMap;
import java.util.Map;

import com.h4d1.rule.entity.CategoryRule;
import com.h4d1.rule.entity.Rule;
import com.h4d1.rule.entity.ValueRule;
import com.h4d1.rule.entity.ViewpointRule;

import lombok.Data;

@Data
public class RuleCompareResult {
	private Rule originRule;
	private Rule rule;
	
	private Map<String, ViewpointRule> originViewpointRuleMap;
	private Map<String, ViewpointRule> viewpointRuleMap;
	private Map<String, CategoryRule> originCategoryRuleMap;
	private Map<String, CategoryRule> categoryRuleMap;
	private Map<String, ValueRule> originValueRuleMap;
	private Map<String, ValueRule> valueRuleMap;

	private boolean isStructureEqual;
	
	public RuleCompareResult() {
		originViewpointRuleMap = new HashMap<String, ViewpointRule>();
		viewpointRuleMap = new HashMap<String, ViewpointRule>();
		originCategoryRuleMap = new HashMap<String, CategoryRule>();
		categoryRuleMap = new HashMap<String, CategoryRule>();
		originValueRuleMap = new HashMap<String, ValueRule>();
		valueRuleMap = new HashMap<String, ValueRule>();
	}
	
	public RuleCompareResult(Rule originRule, Rule rule) {
		this.originRule = originRule;
		this.rule = rule;
		
		originViewpointRuleMap = new HashMap<String, ViewpointRule>();
		viewpointRuleMap = new HashMap<String, ViewpointRule>();
		originCategoryRuleMap = new HashMap<String, CategoryRule>();
		categoryRuleMap = new HashMap<String, CategoryRule>();
		originValueRuleMap = new HashMap<String, ValueRule>();
		valueRuleMap = new HashMap<String, ValueRule>();
		
		for (ViewpointRule viewpointRule : originRule.getViewpointRules()) {
			originViewpointRuleMap.put(viewpointRule.getId(), viewpointRule);
			
			for (CategoryRule categoryRule : viewpointRule.getCategoryRules()) {
				originCategoryRuleMap.put(categoryRule.getId(), categoryRule);
				
				ValueRule valueRule = categoryRule.getValueRule();
				
				originValueRuleMap.put(categoryRule.getId(), valueRule);
			}
		}
		
		for (ViewpointRule viewpointRule : rule.getViewpointRules()) {
			viewpointRuleMap.put(viewpointRule.getId(), viewpointRule);
			
			for (CategoryRule categoryRule : viewpointRule.getCategoryRules()) {
				categoryRuleMap.put(categoryRule.getId(), categoryRule);
				
				ValueRule valueRule = categoryRule.getValueRule();
				
				valueRuleMap.put(categoryRule.getId(), valueRule);
			}
		}	
	}
}
