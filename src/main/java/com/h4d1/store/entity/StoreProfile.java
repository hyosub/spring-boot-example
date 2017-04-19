package com.h4d1.store.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.h4d1.entity.Base;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "STORE_PROFILE")
@AttributeOverride(name = "id", column = @Column(name = "STORE_PROFILE_ID", length = 32))
@JsonIgnoreProperties({ "createTime", "updateTime" })
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class StoreProfile extends Base {

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ADDRESS_ID")
	@JsonProperty("address")
	@JsonInclude(Include.NON_NULL)
	private StoreAddress storeAddress;
	
	//TODO 유효한 전화 번호 값을 어떻게 체크할지 고민 (제대로 구현한다면 국가코드와 기타 지역번호 등도 다 나누는 것이 좋을 듯 하지만..)
	@Column(name = "PHONE_NUMBER", length = 30, nullable = true)
	@JsonProperty("phone")
	@JsonInclude(Include.NON_NULL)
	private String phoneNumber;
	
	//상점 설명의 경우 최대 길이 제한이 없으므로 CLOB 타입으로 설정
	@Column(name = "DESCRIPTION", nullable = true)
	@Lob
	private String description;
	
	public StoreProfile() {
	
	}
	
	public StoreProfile(String id) {
		initMandatoryFieldValue(id);
	}
}
