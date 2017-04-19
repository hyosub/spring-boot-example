package com.h4d1.store.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h4d1.exception.ServiceException;
import com.h4d1.store.StorePhotoService;
import com.h4d1.store.entity.StorePhoto;
import com.h4d1.store.repository.StorePhotoRepository;
import com.h4d1.util.UuidGenerator;

@Service
@Transactional
public class PhotoManager implements StorePhotoService {
	
	/**
	 * PK인 uuid를 생성하는 서비스
	 */
	@Autowired
	private UuidGenerator uuidGenerator;
	
	/**
	 * 상점 정보를 관리하는 JpaRepository
	 */
	@Autowired
	private StorePhotoRepository storePhotoRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(PhotoManager.class);

	@Override
	public void createStorePhoto(StorePhoto storePhoto) throws ServiceException {
		logger.info("Create a store-photo. [storePhoto={}]", storePhoto);
		
		String id = uuidGenerator.generateUUID();
		
		storePhoto.initMandatoryFieldValue(id);
		
		storePhotoRepository.save(storePhoto);
	}
	
	@Override
	public StorePhoto getDefaultStorePhoto(String storeId) throws ServiceException {
		logger.info("Get a default store-photo. [storeId={}]", storeId);
		return storePhotoRepository.findByStoreIdAndIsDefaultTrue(storeId);
	}
	
	@Override
	public StorePhoto getStorePhoto(String photoId) throws ServiceException {
		logger.info("Get a store-photo. [photoId={}]", photoId);
		return storePhotoRepository.findOne(photoId);
	}

	@Override
	public List<StorePhoto> getStorePhotos(String storeId) throws ServiceException {
		logger.info("Get a store-photos. [storeId={}]", storeId);
		return storePhotoRepository.findByStoreId(storeId);
	}
}
