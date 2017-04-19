package com.h4d1.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class Base {
	
	@Id
	@Column(name = "ID", length = 32)
	protected String id;
	
	@Column(name = "CREATE_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createTime;
	
	@Column(name = "UPDATE_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date updateTime;
	
	public Base() {
		
	}
	
	@JsonFormat(pattern = "yyyyMMddHHmmss")
	public Date getCreateTime(){
		return updateTime;
	}
	@JsonFormat(pattern = "yyyyMMddHHmmss")
	public Date getUpdateTime(){
		return updateTime;
	}
	
	public void initMandatoryFieldValue(String id) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
}
