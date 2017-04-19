package com.h4d1.store.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.h4d1.store.entity.StoreEvent;

public interface StoreEventRepository extends JpaRepository<StoreEvent, String> {
	@Query("select se from StoreEvent se where se.store.id = :storeId and se.endTime > :currentTime")
	StoreEvent findByStoreIdEqualsAndEndTimeGreaterThan(@Param("storeId") String storeId, @Param("currentTime") Date currentTime);
}
