package com.h4d1.util.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.h4d1.rule.entity.CategoryRule;
import com.h4d1.rule.entity.type.CategoryType;

import lombok.Data;

@Data
public class CategoryRuleVo {
	
	private String id;
	private CategoryType type;
	private int reflectionRate;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	@JsonBackReference
	private ViewpointRuleVo viewpointRule;
	
	private ValueRuleVo valueRule;
	
	public CategoryRuleVo() {
	
	}
	
	public void updateFields(CategoryRule categoryRule, ViewpointRuleVo viewpointRuleVo) {
		if (categoryRule != null) {
			this.id = categoryRule.getId();
			this.type = categoryRule.getType();
			this.reflectionRate = categoryRule.getReflectionRate();
			this.createTime = categoryRule.getCreateTime();
			this.updateTime = categoryRule.getUpdateTime();
			
			viewpointRule = viewpointRuleVo;
			
			if (categoryRule.getValueRule() != null) {
				valueRule = new ValueRuleVo();
				valueRule.updateFields(categoryRule.getValueRule());
			}
		}
	}
	
	public CategoryRuleVo(String id, int reflectionRate) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.reflectionRate = reflectionRate;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
}
