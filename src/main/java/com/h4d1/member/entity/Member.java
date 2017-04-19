package com.h4d1.member.entity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.h4d1.entity.Base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "MEMBER")
@AttributeOverride(name = "id", column = @Column(name = "MEMBER_ID", length = 32))
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class Member extends Base {
	
	@Column(name = "EMAIL", length = 100, nullable = false)
	private String email;
	
	@Column(name = "NAME", length = 100, nullable = false)
	private String name;
	
	public Member() {
	
	}
	
	public Member(String id) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
}
