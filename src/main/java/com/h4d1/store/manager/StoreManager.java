package com.h4d1.store.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h4d1.exception.ServiceException;
import com.h4d1.exception.type.ServiceError;
import com.h4d1.store.StoreService;
import com.h4d1.store.entity.Store;
import com.h4d1.store.entity.StoreAddress;
import com.h4d1.store.entity.StoreProfile;
import com.h4d1.store.repository.StoreRepository;
import com.h4d1.store.value.entity.StoreValue;
import com.h4d1.util.UuidGenerator;
import com.h4d1.util.json.StoreSummaryVo;
import com.h4d1.util.json.StoreVo;

@Service
@Transactional
public class StoreManager implements StoreService {
	
	/**
	 * PK인 uuid를 생성하는 서비스
	 */
	@Autowired
	private UuidGenerator uuidGenerator;
	
	/**
	 * 상점 정보를 관리하는 JpaRepository
	 */
	@Autowired
	private StoreRepository storeRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(StoreManager.class);
	
	@Override
	public void createStore(Store store) throws ServiceException {
		logger.info("Create a store. [store={}]", store);
		
		//상점 주소 정보
		//StoreAddress storeAddress = null;
		//상점 세부 정보
		StoreProfile storeProfile = store.getStoreProfile();
		
		//상점 아이디 생성
		String storeId = uuidGenerator.generateUUID();
		//상점 아이디 정보 및 생성 시간, 갱신 시간 업데이트
		store.initMandatoryFieldValue(storeId);
		
		//상점 세부 정보도 함께 생성 요청하였을 경우
		if (storeProfile != null) {
			StoreAddress storeAddress = storeProfile.getStoreAddress();
			
			//상점 세부 정보 중 주소 정보를 생성 요청하였을 경우
			if (storeAddress != null) {
				//상점 주소 정보 아이디 생성
				String storeAddressId = uuidGenerator.generateUUID();
				//상점 주소 정보 아이디 생성
				storeAddress.initMandatoryFieldValue(storeAddressId);
			}
			
			//상점 세부 정보 아이디 생성
			String storeProfileId = uuidGenerator.generateUUID();
			//상점 세부 정보 아이디 및 생성 시간, 갱신 시간 업데이트
			storeProfile.initMandatoryFieldValue(storeProfileId);
		}
		
		//상점 아이디 생성
		String storeValueId = uuidGenerator.generateUUID();
		
		//상점 평가 가치 정보 생성
		StoreValue storeValue = new StoreValue(storeValueId);
		
		//상점 엔티티와 연관 관계를 설정
		store.setStoreValue(storeValue);
		
		logger.info("Save an entity. [entityType={}][value={}]", Store.class.getName(), store);
		
		//해당 엔티티 저장
		storeRepository.save(store);
	}
	
	@Override
	public Store getStore(String id) throws ServiceException {
		logger.info("Get a store. [id={}]", id);
		
		Store store = storeRepository.findOne(id);
		
		if (store != null) {
			store.incrementVisitCount();
			storeRepository.save(store);
		} else {
			throw new ServiceException(ServiceError.NOT_FOUND);
		}
		
		return store;
	}
	
	@Override
	public List<Store> getStores(Pageable pageable) throws ServiceException {
		logger.info("Get stores. [pageable={}]", pageable);
		
		Page<Store> storesPage = storeRepository.findAll(pageable);
		List<Store> stores = storesPage.getContent();
		return stores;
	}
	
	@Override
	public List<Store> getAllStores() throws ServiceException {
		logger.info("Get all stores.");
		
		return storeRepository.findAll();
	}
	

	@Override
	public List<StoreSummaryVo> getStoreSummaries(Pageable pageable) throws ServiceException {
		logger.info("Get stores. [pageable={}]", pageable);
		
		Page<Store> storesPage = storeRepository.findAll(pageable);
		List<Store> stores = storesPage.getContent();

		List<StoreSummaryVo> storeSumarries = new ArrayList<StoreSummaryVo>();
		
		for (Store store : stores) {
			StoreVo storeVo = new StoreVo();
			storeVo.updateFields(store);
			
			storeSumarries.add(storeVo.getStoreSummary());
		}
		
		return storeSumarries;
	}
}
