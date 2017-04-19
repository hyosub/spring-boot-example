package com.h4d1.store;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.h4d1.exception.ServiceException;
import com.h4d1.store.entity.Store;
import com.h4d1.util.json.StoreSummaryVo;


/**
 * <p>StoreService는 상점관리를 위한 기능들을 정의한다.</p>
 * 
 * <p>StoreService는  이하 Entity들과 연관이 있다.<br>
 * 1. Store(상점 정보)<br>
 * 2. StorePhoto(상점 사진 정보)</p>
 * 
 * @author 임효섭
 * @since 2017-01-21
 */
public interface StoreService {
	
	void createStore(Store store) throws ServiceException;
	
	/**
	 * 특정 상점 정보를 검색한다.
	 * 
	 * @param id 상점의 식별 엔티티 아이디(PK)
	 * @return {@link Store} 검색된 상점 정보
	 * @throws ServiceException 비즈니스 로직 수행 시 발생(일반 Exception 클래스를 예외전환함)  
	 */
	Store getStore(String id) throws ServiceException;
	
	/**
	 * 상점 정보들을 검색한다.
	 *
	 * @param pageable 페이징 처리 및 정렬 처리를 위한 인터페이스
	 * @return {@link List} 검색된 상점 정보들을 페이징 및 정렬 처리한 결과
	 * @throws ServiceException 비즈니스 로직 수행 시 발생(일반 Exception 클래스를 예외전환함)  
	 */
	List<Store> getStores(Pageable pageable) throws ServiceException;
	
	List<Store> getAllStores() throws ServiceException;
	
	/**
	 * 상점 요약 정보들을 검색한다.
	 *
	 * @param pageable 페이징 처리 및 정렬 처리를 위한 인터페이스
	 * @return {@link List} 검색된 상점 정보들을 페이징 및 정렬 처리한 결과
	 * @throws ServiceException 비즈니스 로직 수행 시 발생(일반 Exception 클래스를 예외전환함)  
	 */
	List<StoreSummaryVo> getStoreSummaries(Pageable pageable) throws ServiceException;
}
