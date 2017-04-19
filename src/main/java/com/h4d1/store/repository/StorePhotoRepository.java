package com.h4d1.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.h4d1.store.entity.StorePhoto;

public interface StorePhotoRepository extends JpaRepository<StorePhoto, String> {
	@Query("select sp from StorePhoto sp where sp.store.id = :storeId")
	List<StorePhoto> findByStoreId(@Param("storeId") String storeId);
	
	@Query("select sp from StorePhoto sp where sp.store.id = :storeId and sp.isDefault = true")
	StorePhoto findByStoreIdAndIsDefaultTrue(@Param("storeId") String storeId);
}
