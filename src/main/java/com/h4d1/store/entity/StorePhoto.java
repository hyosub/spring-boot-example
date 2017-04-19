package com.h4d1.store.entity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.h4d1.entity.Base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "STORE_PHOTO")
@AttributeOverride(name = "id", column = @Column(name = "STORE_PHOTO_ID", length = 32))
@EqualsAndHashCode(callSuper = true, of = {})
@Data
public class StorePhoto extends Base {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private Store store;
	
	@Column(name = "URI", length = 100, nullable = false)
	private String uri;
	
	@Column(name = "IS_SELF_PICTURED", nullable = false)
	private boolean isSelfPictured;
	
	@Column(name = "IS_DEFAULT", nullable = false)
	private boolean isDefault;
	
	public StorePhoto() {
		
	}
	
	public StorePhoto(String id) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
	}
	
	public StorePhoto(String id, boolean isDefault) {
		//생성 시간을 기록
		Date createTime = new Date();
		
		this.id = id;
		this.createTime = createTime;
		//초기에는 생성 시간과 동일하도록 설정
		updateTime = createTime;
		
		//기본 사진인지 여부를 주어진 값으로 설정
		this.isDefault = isDefault;
	}
	
	/*public void setStore(Store store) {
		//기존과의 연관성을 제거함.
		if (this.store != null) {
			this.store.getStorePhotos().remove(this);
		}
		
		this.store = store;
		
		//새로운 연관 관계를 추가
		store.getStorePhotos().add(this);
	}*/
}
