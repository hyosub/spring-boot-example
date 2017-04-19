package com.h4d1.store.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.h4d1.entity.Base;
import com.h4d1.store.entity.type.StoreType;
import com.h4d1.store.value.entity.StoreValue;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Table(name = "STORE")
@AttributeOverride(name = "id", column = @Column(name = "STORE_ID", length = 32))
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class Store extends Base {
	@Getter(value = AccessLevel.NONE)
	private final String COLUMN_DEF_VISIT_COUNT = "INT DEFAULT 0";
	
	@Getter(value = AccessLevel.NONE)
	private final String COLUMN_DEF_LIKE_COUNT = "INT DEFAULT 0";
	
	@Getter(value = AccessLevel.NONE)
	private final String COLUMN_DEF_USER_RATING_AVERAGE = "FLOAT DEFAULT 0";
	
	@Getter(value = AccessLevel.NONE)
	private final int DEFAULT_VALUE_VISIT_COUNT = 0;
	
	@Getter(value = AccessLevel.NONE)
	private final int DEFAULT_VALUE_LIKE_COUNT = 0;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_PROFILE_ID")
	private StoreProfile storeProfile;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_VALUE_ID")
	@JsonInclude(Include.NON_NULL)
	private StoreValue storeValue;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "STORE_ID")
	private List<StorePhoto> storePhotos = new ArrayList<StorePhoto>();
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "STORE_ID")
	private List<StoreEvent> storeEvents = new ArrayList<StoreEvent>();
	
	@Column(name = "NAME", length = 100, nullable = false)
	private String name;
	
	@Column(name = "TYPE", length = 10, nullable = false)
	@Enumerated(EnumType.STRING)
	private StoreType type;
	
	//엔티티를 따로 만들까도 생각을 해봤으나, 굳이 한 번 더 탐색을 할 필요가 있나 하는 생각에 상점 엔티티에 포함 시킴
	@Column(name = "VISIT_COUNT", nullable = false, columnDefinition = COLUMN_DEF_VISIT_COUNT)
	private int visitCount;
	
	//엔티티를 따로 만들까도 생각을 해봤으나, 굳이 한 번 더 탐색을 할 필요가 있나 하는 생각에 상점 엔티티에 포함 시킴
	@Column(name = "LIKE_COUNT", nullable = false, columnDefinition = COLUMN_DEF_LIKE_COUNT)
	private int likeCount;
	
	//엔티티를 따로 만들까도 생각을 해봤으나, 굳이 한 번 더 탐색을 할 필요가 있나 하는 생각에 상점 엔티티에 포함 시킴
	@Column(name = "USER_RATING_AVERAGE", nullable = false, columnDefinition = COLUMN_DEF_USER_RATING_AVERAGE)
	private float userRatingAverage;
	
	public Store() {
		 
	}
	
	/*public Store(String id) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}*/
	
	@JsonCreator
	public Store(
			@JsonProperty("name") String name) {
		this.name = name;
		
		//초기 상점 타입은 소형
		type = StoreType.SMALL;
		//상점 조회 수 및 좋아요 수 0으로 설정
		visitCount = DEFAULT_VALUE_VISIT_COUNT;
		likeCount = DEFAULT_VALUE_LIKE_COUNT;
		
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
	
	@Override
	public void initMandatoryFieldValue(String id) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		//초기 상점 타입은 소형
		type = StoreType.SMALL;
		//상점 조회 수 및 좋아요 수 0으로 설정
		visitCount = DEFAULT_VALUE_VISIT_COUNT;
		likeCount = DEFAULT_VALUE_LIKE_COUNT;
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
	
	public boolean isEvent() {
		//이벤트 기본 값은 false (이벤트 중일 때만 검색하면 됨)
		boolean isEvent = false;
		
		//현재 시간
		Date currentTime = new Date();
		
		if (storeEvents != null) {
			for (StoreEvent storeEvent : storeEvents) {
				//이벤트 종료 시간이 현재 시간보다 미래인 경우 (이벤트 중)
				if (storeEvent.getEndTime().getTime() > currentTime.getTime()) {
					isEvent = true;
				}
			}
		} 
		
		return isEvent;
	}
	
	public StorePhoto getDefaultStorePhoto() {
		StorePhoto defaultStorePhoto = null;
		
		if (storePhotos != null) {
			for (StorePhoto storePhoto : storePhotos) {
				if (storePhoto.isDefault()) {
					defaultStorePhoto = storePhoto;
				}
			}
		}
		
		return defaultStorePhoto;
	}
	
	public void addStorePhoto(StorePhoto storePhoto) {
		if (storePhotos != null) {
			if (storePhoto != null) {
				storePhotos.add(storePhoto);
			}
		}
	}
	
	public void addStoreEvent(StoreEvent storeEvent) {
		if (storeEvents != null) {
			if (storeEvent != null) {
				storeEvents.add(storeEvent);
			}
		}
	}
	
	public void incrementVisitCount () {
		visitCount += 1;
	}
	
	public void incrementLikeCount () {
		likeCount += 1;
	}
}
