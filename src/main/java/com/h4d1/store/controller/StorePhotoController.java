package com.h4d1.store.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.h4d1.exception.ServiceException;
import com.h4d1.exception.type.ServiceError;
import com.h4d1.store.StorePhotoService;
import com.h4d1.store.entity.StorePhoto;
import com.h4d1.util.json.StorePhotoVo;
import com.h4d1.util.json.response.GetStorePhotosResponse;

@RestController
@RequestMapping(value="/photos")
public class StorePhotoController {
	
	@Autowired
	private StorePhotoService storePhotoService;
	
	private static final Logger logger = LoggerFactory.getLogger(StorePhotoController.class);
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	@ResponseBody
	public StorePhotoVo getStorePhoto(@PathVariable(required=true) String id) throws ServiceException {
		try {
			logger.info("Check request-param. [id={}]", id);
			
			//서비스로부터 조건에 맞게 검색한 상점 사진 정보 정보를 가져옴
			StorePhoto storePhoto = storePhotoService.getStorePhoto(id);
			
			//결과 리턴 (Jackson이 자동으로 json을 생성)
			StorePhotoVo storePhotoVo = null;
			
			if (storePhoto != null) {
				storePhotoVo = new StorePhotoVo();
				storePhotoVo.updateFields(storePhoto, null);
			} else {
				//Not found exception 처리
				throw new ServiceException(ServiceError.NOT_FOUND);
			}
			
			return storePhotoVo;
		} catch (ServiceException e) {
			logger.error("ServiceException occured. Handle exception.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public GetStorePhotosResponse getStorePhotos(@RequestParam(name="storeId", required=true) String storeId) throws ServiceException {
		try {
			logger.info("Check request-param. [storeId={}]", storeId);
			
			//서비스로부터 조건에 맞게 검색한 상점 사진 정보 목록 정보를 가져옴
			List<StorePhoto> storePhotos = storePhotoService.getStorePhotos(storeId);
			List<StorePhotoVo> storePhotoVos = new ArrayList<StorePhotoVo>(); 
			
			if (storePhotos.size() > 0) {
				for (StorePhoto storePhoto : storePhotos) {
					StorePhotoVo storePhotoVo = new StorePhotoVo();
					storePhotoVo.updateFields(storePhoto, null);
					storePhotoVos.add(storePhotoVo);
				}
			} else {
				//Not found exception 처리
				throw new ServiceException(ServiceError.NOT_FOUND);
			}
			
			//결과 리턴 (Jackson이 자동으로 json을 생성)
			return new GetStorePhotosResponse(storePhotoVos);
		} catch (ServiceException e) {
			logger.error("ServiceException occured. Handle exception.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
}
