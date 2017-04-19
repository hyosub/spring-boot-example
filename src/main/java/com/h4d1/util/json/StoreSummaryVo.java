package com.h4d1.util.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StoreSummaryVo {
	private String id;
	private String name;
	
	@JsonInclude(Include.NON_NULL)
	private StorePhotoVo defaultStorePhoto;
	
	private boolean isEvent;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	public StoreSummaryVo(){
		
	}
	
	public StoreSummaryVo(String id, String name, StorePhotoVo defaultStorePhoto, boolean isEvent, Date createTime, Date updateTime){
		this.id = id;
		this.name = name;
		this.defaultStorePhoto = defaultStorePhoto;
		this.isEvent = isEvent;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	
	@JsonProperty("isEvent")
	public boolean isEvent() {
		return isEvent;
	}
}
