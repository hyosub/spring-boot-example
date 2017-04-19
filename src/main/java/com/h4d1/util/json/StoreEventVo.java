package com.h4d1.util.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.h4d1.store.entity.StoreEvent;

import lombok.Data;

@Data
public class StoreEventVo {
	private String id;
	
	@JsonBackReference
	private StoreVo store;
	
	private String description;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	
	public StoreEventVo() {
		
	}
	
	public StoreEventVo(String id, Date startTime, Date endTime) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
		//이벤트 시작 시간 설정
		this.startTime = startTime;
		//이벤트 종료 시간 설정
		this.endTime = endTime;
	}
	
	public void updateFields(StoreEvent storeEvent, StoreVo storeVo) {
		if (storeEvent != null) {
			id = storeEvent.getId();
			store = storeVo;
			description = storeEvent.getDescription();
			createTime = storeEvent.getCreateTime();
			updateTime = storeEvent.getUpdateTime();
			startTime = storeEvent.getStartTime();
			endTime = storeEvent.getEndTime();
		}
	}
}
