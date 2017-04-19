package com.h4d1.member.entity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.h4d1.entity.Base;
import com.h4d1.store.entity.Store;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Table(name = "USER_RATINGS")
@AttributeOverride(name = "id", column = @Column(name = "USER_RATING_ID", length = 32))
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class UserRating extends Base {
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name = "STORE_ID")
	private Store store;
	
	@Column(name = "RATING_SCORE", nullable = false, columnDefinition="TINYINT DEFAULT 0")
	private int ratingScore;
	
	public UserRating() {
		
	}
	
	public UserRating(String id) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
}
