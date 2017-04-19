package com.h4d1.store.value.entity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.h4d1.entity.Base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "STORE_VALUE")
@AttributeOverride(name = "id", column = @Column(name = "STORE_VALUE_ID", length = 32))
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class StoreValue extends Base {
	
	@Column(name = "VALUE", nullable = false)
	private float value;
	
	@Column(name = "IS_EVALUATED", nullable = false)
	private boolean isEvaluated;
	
	public StoreValue() {
		
	}
	
	public StoreValue(String id) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
	
	@JsonProperty("isEvaluated")
	public boolean isEvaluated() {
		return isEvaluated;
	}
}
