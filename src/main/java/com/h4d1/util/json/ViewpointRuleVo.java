package com.h4d1.util.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.h4d1.rule.entity.CategoryRule;
import com.h4d1.rule.entity.ViewpointRule;
import com.h4d1.rule.entity.type.ViewpointType;

import lombok.Data;

@Data
public class ViewpointRuleVo {
	private String id;
	private ViewpointType type;
	private int reflectionRate;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	@JsonBackReference
	private RuleVo rule;
	
	private List<CategoryRuleVo> categoryRules = new ArrayList<CategoryRuleVo>();
	
	public ViewpointRuleVo() {
		
	}
	
	public ViewpointRuleVo(String id, int reflectionRate) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.reflectionRate = reflectionRate;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
	
	public void updateFields(ViewpointRule viewpointRule, RuleVo ruleVo) {
		if (viewpointRule != null) {
			this.id = viewpointRule.getId();
			this.type = viewpointRule.getType();
			this.reflectionRate = viewpointRule.getReflectionRate();
			this.createTime = viewpointRule.getCreateTime();
			this.updateTime = viewpointRule.getUpdateTime();
			
			rule = ruleVo;
			
			if (viewpointRule.getCategoryRules() != null) {
				for (CategoryRule categoryRule : viewpointRule.getCategoryRules()) {
					CategoryRuleVo categoryRuleVo = new CategoryRuleVo();
					categoryRuleVo.updateFields(categoryRule, this);
					categoryRules.add(categoryRuleVo);
				}
			}
		}
	}
	
	public void addCategoryRule(CategoryRuleVo categoryRule) {
		if (categoryRules != null) {
			if (categoryRule != null) {
				categoryRules.add(categoryRule);
			}
		}
	}
}
