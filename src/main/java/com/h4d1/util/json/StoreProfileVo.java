package com.h4d1.util.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.h4d1.store.entity.StoreProfile;

import lombok.Data;

@Data
public class StoreProfileVo {
	
	private String id;
	
	@JsonInclude(Include.NON_NULL)
	private StoreAddressVo storeAddress;
	
	@JsonInclude(Include.NON_NULL)
	private String phoneNumber;
	
	private String description;
	
	@JsonIgnore
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	public StoreProfileVo() {
	
	}
	
	public StoreProfileVo(String id) {
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		updateTime = createTime;
	}
	
	public void updateFields(StoreProfile storeProfile) {
		if (storeProfile != null) {
			id = storeProfile.getId();
			
			if (storeProfile.getStoreAddress() != null) {
				storeAddress = new StoreAddressVo();
				storeAddress.updateFields(storeProfile.getStoreAddress());
			}
			
			phoneNumber = storeProfile.getPhoneNumber();
			description = storeProfile.getDescription();
			createTime = storeProfile.getCreateTime();
			updateTime = storeProfile.getUpdateTime();
		}
	}
}
