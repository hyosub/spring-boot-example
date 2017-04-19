package com.h4d1.util.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.h4d1.store.value.entity.StoreValue;

import lombok.Data;

@Data
public class StoreValueVo {
	private String id;
	private float value;
	private boolean isEvaluated;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	public StoreValueVo() {
		
	}
	
	public StoreValueVo(String id) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
	
	public void updateFields(StoreValue storeValue) {
		if (storeValue != null) {
			this.id = storeValue.getId();
			this.value = storeValue.getValue();
			this.isEvaluated = storeValue.isEvaluated();
			this.createTime = storeValue.getCreateTime();
			this.updateTime = storeValue.getUpdateTime();
		}
	}
	
	@JsonProperty("isEvaluated")
	public boolean isEvaluated() {
		return isEvaluated;
	}
}
