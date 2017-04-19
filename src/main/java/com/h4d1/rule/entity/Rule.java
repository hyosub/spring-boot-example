package com.h4d1.rule.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.h4d1.entity.Base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "RULE")
@AttributeOverride(name = "id", column = @Column(name = "RULE_ID", length = 32))
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class Rule extends Base {
	
	@Column(name = "REFLECTION_RATE_TOTAL", nullable = false, columnDefinition="TINYINT DEFAULT 100")
	private int reflectionRateTotal;
	
	@Column(name = "RATING_SCORE_MAX", nullable = false, columnDefinition="SMALLINT DEFAULT 0")
	private int ratingScoreMax;
	
	@Column(name = "IS_ACTIVE", nullable = false)
	private boolean isActive;
	
	@Column(name = "DISCARD_TIME", nullable = true)
	@JsonInclude(Include.NON_NULL)
	private Date discardTime;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "RULE_ID")
	private List<ViewpointRule> viewpointRules = new ArrayList<ViewpointRule>();
	
	public Rule() {
		
	}
	
	public Rule(String id, int reflectionRateTotal, int ratingScoreMax) {
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
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
		
		if (isActive) {
			discardTime = null;
		} else {
			discardTime = new Date();
		}
	}
	
	public void addViewpointRule(ViewpointRule viewpointRule) {
		if (viewpointRules != null) {
			if (viewpointRule != null) {
				viewpointRules.add(viewpointRule);
			}
		}
	}
}
