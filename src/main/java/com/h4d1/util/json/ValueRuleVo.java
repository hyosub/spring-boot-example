package com.h4d1.util.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.h4d1.rule.entity.ValueRule;
import com.h4d1.rule.entity.type.ValueType;

import lombok.Data;

@Data
public class ValueRuleVo {
	private String id;
	private ValueType type;
	private String firstRankCondition;
	private String secondRankCondition;
	private String thirdRankCondition;
	private int firstRankValue;
	private int secondRankValue;
	private int thirdRankValue;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	public ValueRuleVo() {
	
	}
	
	public ValueRuleVo(String id) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
	
	public void updateFields(ValueRule valueRule) {
		if (valueRule != null) {
			this.id = valueRule.getId();
			this.type = valueRule.getType();
			this.firstRankCondition = valueRule.getFirstRankCondition();
			this.secondRankCondition = valueRule.getSecondRankCondition();
			this.thirdRankCondition = valueRule.getThirdRankCondition();
			this.firstRankValue = valueRule.getFirstRankValue();
			this.secondRankValue = valueRule.getSecondRankValue();
			this.thirdRankValue = valueRule.getThirdRankValue();
			this.createTime = valueRule.getCreateTime();
			this.updateTime = valueRule.getUpdateTime();
		}
	}
}
