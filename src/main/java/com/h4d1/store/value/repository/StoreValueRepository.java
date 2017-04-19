package com.h4d1.store.value.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h4d1.store.value.entity.StoreValue;

public interface StoreValueRepository extends JpaRepository<StoreValue, String> {
	
}
