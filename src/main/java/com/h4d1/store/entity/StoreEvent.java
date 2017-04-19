package com.h4d1.store.entity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.h4d1.entity.Base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "STORE_EVENT")
@AttributeOverride(name = "id", column = @Column(name = "STORE_EVENT_ID", length = 32))
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class StoreEvent extends Base {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private Store store;
	
	//상점 이벤트 설명의 경우 최대 길이 제한이 없으므로 CLOB 타입으로 설정
	@Column(name = "DESCRIPTION", nullable = true)
	@Lob
	private String description;
	
	@Column(name = "EVENT_START_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Column(name = "EVENT_END_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	public StoreEvent() {
		
	}
	
	public StoreEvent(String id, Date startTime, Date endTime) {
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
}
