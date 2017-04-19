package com.h4d1.store.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.h4d1.entity.Base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "STORE_ADDRESS")
@AttributeOverride(name = "id", column = @Column(name = "STORE_ADDRESS_ID", length = 32))
@JsonIgnoreProperties({ "createTime", "updateTime" })
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class StoreAddress extends Base {
	
	@Column(name = "STREET", length = 100, nullable = true)
	private String street;
	
	@Column(name = "DETAIL", length = 100, nullable = true)
	private String detail;
	
	public StoreAddress() {
	
	}
	
	public StoreAddress(String id) {
		initMandatoryFieldValue(id);
	}
}
