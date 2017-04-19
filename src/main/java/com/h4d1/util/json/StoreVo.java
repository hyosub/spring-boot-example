package com.h4d1.util.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.h4d1.store.entity.Store;
import com.h4d1.store.entity.StoreEvent;
import com.h4d1.store.entity.StorePhoto;
import com.h4d1.store.entity.type.StoreType;

import lombok.Data;

@Data
public class StoreVo {
	
	private String id;
	
	@JsonProperty("profile")
	@JsonInclude(Include.NON_NULL)
	private StoreProfileVo storeProfile;
	
	@JsonIgnore
	private List<StorePhotoVo> storePhotos = new ArrayList<StorePhotoVo>();
	
	@JsonIgnore
	private List<StoreEventVo> storeEvents = new ArrayList<StoreEventVo>();
	
	@JsonInclude(Include.NON_NULL)
	private StoreValueVo storeValue;
	
	private String name;
	private StoreType type;
	private int visitCount;
	private int likeCount;
	private float userRatingAverage;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	public StoreVo() {
		 
	}
	
	public StoreVo(String id) {
		 Date createTime = new Date();
		 
		 this.id = id;
		 this.createTime = createTime;
		 this.updateTime = createTime;
	}
	
	public void updateFields(Store store) {
		 if (store != null) {
			 id = store.getId();
			 
			 if (store.getStoreProfile() != null) {
				 storeProfile = new StoreProfileVo();
				 storeProfile.updateFields(store.getStoreProfile());
			 }
			 
			 if (store.getStorePhotos() != null) {
				 for (StorePhoto storePhoto : store.getStorePhotos()) {
					 StorePhotoVo storePhotoVo = new StorePhotoVo();
					 storePhotoVo.updateFields(storePhoto, this);
					 storePhotos.add(storePhotoVo);
				 }
			 }
			 
			 if (store.getStoreEvents() != null) {
				 for (StoreEvent storeEvent : store.getStoreEvents()) {
					 StoreEventVo storeEventVo = new StoreEventVo();
					 storeEventVo.updateFields(storeEvent, this);
					 storeEvents.add(storeEventVo);
				 }
			 }
			 
			 if (store.getStoreValue() != null) {
				 storeValue = new StoreValueVo();
				 storeValue.updateFields(store.getStoreValue());
			 }
			 
			 name = store.getName();
			 type = store.getType();
			 visitCount = store.getVisitCount();
			 likeCount = store.getLikeCount();
			 userRatingAverage = store.getUserRatingAverage();
			 createTime = store.getCreateTime();
			 updateTime = store.getUpdateTime();
		 }
	}
	
	@JsonProperty("isEvent")
	public boolean isEvent() {
		//이벤트 기본 값은 false (이벤트 중일 때만 검색하면 됨)
		boolean isEvent = false;
		
		//현재 시간
		Date currentTime = new Date();
		
		if (storeEvents != null) {
			for (StoreEventVo storeEvent : storeEvents) {
				//이벤트 종료 시간이 현재 시간보다 미래인 경우 (이벤트 중)
				if (storeEvent.getEndTime().getTime() > currentTime.getTime()) {
					isEvent = true;
				}
			}
		} 
		
		return isEvent;
	}
	
	@JsonProperty("activeStoreEvent")
	@JsonInclude(Include.NON_NULL)
	public StoreEventVo getActiveStoreEvent() {
		StoreEventVo activeStoreEvent = null;
		
		//현재 시간
		Date currentTime = new Date();
		
		if (storeEvents != null) {
			for (StoreEventVo storeEvent : storeEvents) {
				//이벤트 종료 시간이 현재 시간보다 미래인 경우 (이벤트 중)
				if (storeEvent.getEndTime().getTime() > currentTime.getTime()) {
					activeStoreEvent = storeEvent;
				}
			}
		}
		
		return activeStoreEvent;
	}
	
	@JsonProperty("defaultStorePhoto")
	@JsonInclude(Include.NON_NULL)
	public StorePhotoVo getDefaultStorePhoto() {
		StorePhotoVo defaultStorePhoto = null;
		
		if (storePhotos != null) {
			for (StorePhotoVo storePhoto : storePhotos) {
				if (storePhoto.isDefault()) {
					defaultStorePhoto = storePhoto;
				}
			}
		}
		
		return defaultStorePhoto;
	}
	
	@JsonIgnore
	public StoreSummaryVo getStoreSummary() {
		return new StoreSummaryVo(id, name, getDefaultStorePhoto(), isEvent(), createTime, updateTime);
	}
}
