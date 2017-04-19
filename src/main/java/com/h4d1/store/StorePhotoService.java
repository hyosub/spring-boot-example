package com.h4d1.store;

import java.util.List;

import com.h4d1.exception.ServiceException;
import com.h4d1.store.entity.StorePhoto;

public interface StorePhotoService {
	
	void createStorePhoto(StorePhoto storePhoto) throws ServiceException;
	
	/**
	 * 특정 상점의 등록된 기본 상점 사진 정보를 검색한다.
	 * 
	 * @param storeId 상점의 식별 엔티티 아이디
	 * @return {@link StorePhoto} 검색된 상점 사진 정보
	 * @throws ServiceException 비즈니스 로직 수행 시 발생(일반 Exception 클래스를 예외전환함)  
	 */
	StorePhoto getDefaultStorePhoto(String storeId) throws ServiceException;
	
	/**
	 * 특정 상점의 등록된 특정 상점 사진 정보를 검색한다.
	 * 
	 * @param id 상점 사진의 식별 엔티티 아이디(PK)
	 * @return {@link StorePhoto} 검색된 상점 사진 정보
	 * @throws ServiceException 비즈니스 로직 수행 시 발생(일반 Exception 클래스를 예외전환함)  
	 */
	StorePhoto getStorePhoto(String id) throws ServiceException;
	
	/**
	 * 특정 상점의 등록된 상점 사진 정보들을 검색한다.
	 * 
	 * @param storeId 상점의 식별 엔티티 아이디
	 * @return {@link List} 검색된 상점 사진 정보들
	 * @throws ServiceException 비즈니스 로직 수행 시 발생(일반 Exception 클래스를 예외전환함)  
	 */
	List<StorePhoto> getStorePhotos(String storeId) throws ServiceException;
}
