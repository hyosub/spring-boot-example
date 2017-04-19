package com.h4d1.util.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.h4d1.store.entity.StoreAddress;

import lombok.Data;

@Data
public class StoreAddressVo {
	private String id;
	private String street;
	private String detail;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	public StoreAddressVo() {
	
	}
	
	public StoreAddressVo(String id) {
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		updateTime = createTime;
	}
	
	public void updateFields(StoreAddress storeAddress) {
		if (storeAddress != null) {
			id = storeAddress.getId();
			street = storeAddress.getStreet();
			detail = storeAddress.getDetail();
			createTime = storeAddress.getCreateTime();
			updateTime = storeAddress.getUpdateTime();
		}
	}
}
