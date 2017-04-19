package com.h4d1.util.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.h4d1.rule.entity.Rule;
import com.h4d1.rule.entity.ViewpointRule;

import lombok.Data;

@Data
public class RuleVo {
	private String id;
	private int reflectionRateTotal;
	private int ratingScoreMax;
	private boolean isActive;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonInclude(Include.NON_NULL)
	private Date discardTime;
	
	private List<ViewpointRuleVo> viewpointRules = new ArrayList<ViewpointRuleVo>();
	
	public RuleVo() {
		
	}
	
	public RuleVo(String id, int reflectionRateTotal, int ratingScoreMax) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.reflectionRateTotal = reflectionRateTotal;
		this.ratingScoreMax = ratingScoreMax;
		this.isActive = true;
		
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
	
	public void updateFields(Rule rule) {
		if (rule != null) {
			this.id = rule.getId();
			this.reflectionRateTotal = rule.getReflectionRateTotal();
			this.ratingScoreMax = rule.getRatingScoreMax();
			this.isActive = rule.isActive();
			this.createTime = rule.getCreateTime();
			this.updateTime = rule.getUpdateTime();
			this.discardTime = rule.getDiscardTime();
			
			if (rule.getViewpointRules() != null) {
				for (ViewpointRule viewpointRule : rule.getViewpointRules()) {
					ViewpointRuleVo viewpointRuleVo = new ViewpointRuleVo();
					viewpointRuleVo.updateFields(viewpointRule, this);
					viewpointRules.add(viewpointRuleVo);
				}
			}
		}
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
		
		if (isActive) {
			discardTime = null;
		} else {
			discardTime = new Date();
		}
	}
	
	public void addViewpointRule(ViewpointRuleVo viewpointRule) {
		if (viewpointRules != null) {
			if (viewpointRule != null) {
				viewpointRules.add(viewpointRule);
			}
		}
	}
}
