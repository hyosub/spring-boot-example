package com.h4d1.rule.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.h4d1.entity.Base;
import com.h4d1.rule.entity.type.ViewpointType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "VIEWPOINT_RULE")
@AttributeOverride(name = "id", column = @Column(name = "VIEWPOINT_RULE_ID", length = 32))
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class ViewpointRule extends Base {
	
	@Column(name = "TYPE", length = 30, nullable = false)
	@Enumerated(EnumType.STRING)
	private ViewpointType type;
	
	@Column(name = "REFLECTION_RATE", nullable = false, columnDefinition="TINYINT")
	private int reflectionRate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RULE_ID")
	@JsonIgnore
	private Rule rule;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "VIEWPOINT_RULE_ID")
	private List<CategoryRule> categoryRules = new ArrayList<CategoryRule>();
	
	public ViewpointRule() {
		
	}
	
	public ViewpointRule(String id, int reflectionRate) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.reflectionRate = reflectionRate;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
	
	public void addCategoryRule(CategoryRule categoryRule) {
		if (categoryRules != null) {
			if (categoryRule != null) {
				categoryRules.add(categoryRule);
			}
		}
	}
}
