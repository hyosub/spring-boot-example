package com.h4d1.rule.entity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.h4d1.entity.Base;
import com.h4d1.rule.entity.type.ValueType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "VALUE_RULE")
@AttributeOverride(name = "id", column = @Column(name = "VALUE_RULE_ID", length = 32))
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class ValueRule extends Base {
	
	@Column(name = "TYPE", length = 70, nullable = false)
	@Enumerated(EnumType.STRING)
	private ValueType type;
	
	@Column(name = "FIRST_RANK_CONDITION", nullable = true)
	private String firstRankCondition;
	
	@Column(name = "SECOND_RANK_CONDITION", nullable = true)
	private String secondRankCondition;
	
	@Column(name = "THIRD_RANK_CONDITION", nullable = true)
	private String thirdRankCondition;
	
	@Column(name = "FIRST_RANK_VALUE", nullable = true)
	private int firstRankValue;
	
	@Column(name = "SECOND_RANK_VALUE", nullable = true)
	private int secondRankValue;
	
	@Column(name = "THIRD_RANK_VALUE", nullable = true)
	private int thirdRankValue;
	
	public ValueRule() {
	
	}
	
	public ValueRule(String id) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
}
