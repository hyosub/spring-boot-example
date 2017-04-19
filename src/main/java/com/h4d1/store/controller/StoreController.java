package com.h4d1.store.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.h4d1.exception.ServiceException;
import com.h4d1.exception.type.ServiceError;
import com.h4d1.store.StoreService;
import com.h4d1.store.entity.Store;
import com.h4d1.util.json.StoreSummaryVo;
import com.h4d1.util.json.StoreVo;
import com.h4d1.util.json.response.GetStoresResponse;

@RestController
@RequestMapping(value="/stores")
public class StoreController {
	
	@Autowired
	private StoreService storeService;
	
	private static final Logger logger = LoggerFactory.getLogger(StoreController.class);
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public void createStore(@RequestBody(required=true) Store store) throws ServiceException {
		try {
			logger.info("Check request-param. [store={}]", store);
			
			//상점 엔티티 생성
			storeService.createStore(store);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public GetStoresResponse getStores(
			@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) throws ServiceException {
		try {
			logger.info("Check request-param. [pageable={}]", pageable);
			
			//서비스로부터 조건에 맞게 검색한 상점 요약 정보 목록의 페이지 정보를 가져옴
			List<StoreSummaryVo> storeSummaries = storeService.getStoreSummaries(pageable);
			
			//결과 리턴 (Jackson이 자동으로 json을 생성)
			return new GetStoresResponse(storeSummaries);
		} catch (ServiceException e) {
			logger.error("ServiceException occured. Handle exception.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	@ResponseBody
	public StoreVo getStore(@PathVariable(required=true) String id) throws ServiceException {
		try {
			logger.info("Check request-param. [id={}]", id);
			
			//서비스로부터 조건에 맞게 검색한 상점 정보(상점 세부 정보 포함)를 가져옴
			Store store = storeService.getStore(id);
			StoreVo storeVo = null;
			
			if (store != null) {
				//Jackson의 serailize 문제점을 회피하기 위하여 VO객체를 사용.
				storeVo = new StoreVo();
				storeVo.updateFields(store);
			} else {
				//Not found exception 처리
				throw new ServiceException(ServiceError.NOT_FOUND);
			}
			
			return storeVo;
		} catch (ServiceException e) {
			logger.error("ServiceException occured. Handle exception.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
}
