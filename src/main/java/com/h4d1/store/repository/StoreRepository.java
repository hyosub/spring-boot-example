package com.h4d1.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h4d1.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, String> {
	
}
