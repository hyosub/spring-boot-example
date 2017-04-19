package com.h4d1.member.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.h4d1.store.entity.Store;

import lombok.Data;

@Entity
@Table(name = "LIKES")
@Data
public class Like {
	
	@Id
	@Column(name = "LIKE_ID", length = 32)
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name = "STORE_ID")
	private Store store;
	
	@Column(name = "CREATE_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	public Like() {
		
	}
	
	public Like(String id) {
		this.id = id;
		//생성 시간을 기록
		this.createTime = new Date();
	}
}
