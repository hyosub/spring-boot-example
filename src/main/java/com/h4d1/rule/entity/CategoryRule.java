package com.h4d1.rule.entity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.h4d1.entity.Base;
import com.h4d1.rule.entity.type.CategoryType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "CATEGORY_RULE")
@AttributeOverride(name = "id", column = @Column(name = "CATEGORY_RULE_ID", length = 32))
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class CategoryRule extends Base {
	
	@Column(name = "TYPE", length = 30, nullable = false)
	@Enumerated(EnumType.STRING)
	private CategoryType type;
	
	@Column(name = "REFLECTION_RATE", nullable = false, columnDefinition="TINYINT")
	private int reflectionRate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VIEWPOINT_RULE_ID")
	@JsonIgnore
	private ViewpointRule viewpointRule;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "VALUE_RULE_ID")
	private ValueRule valueRule;
	
	public CategoryRule() {
	
	}
	
	public CategoryRule(String id, int reflectionRate) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.reflectionRate = reflectionRate;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
}
