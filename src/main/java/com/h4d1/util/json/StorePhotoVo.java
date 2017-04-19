package com.h4d1.util.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.h4d1.store.entity.StorePhoto;

import lombok.Data;

@Data
public class StorePhotoVo {
	private String id;
	
	@JsonBackReference
	//@JsonInclude(Include.NON_NULL)
	private StoreVo store;
	
	private String uri;
	private boolean isSelfPictured;
	private boolean isDefault;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	public StorePhotoVo() {
		
	}
	
	public StorePhotoVo(String id) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
	
	public StorePhotoVo(String id, boolean isDefault) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
		
		//기본 사진인지 여부를 주어진 값으로 설정
		this.isDefault = isDefault;
	}
	
	public void updateFields(StorePhoto storePhoto, StoreVo storeVo) {
		if (storePhoto != null) {
			id = storePhoto.getId();
			store = storeVo;
			uri = storePhoto.getUri();
			isSelfPictured = storePhoto.isSelfPictured();
			isDefault = storePhoto.isDefault();
			createTime = storePhoto.getCreateTime();
			updateTime = storePhoto.getUpdateTime();
		}
	}
}
