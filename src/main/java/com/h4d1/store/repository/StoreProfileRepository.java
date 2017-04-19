package com.h4d1.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h4d1.store.entity.StoreProfile;

public interface StoreProfileRepository extends JpaRepository<StoreProfile, String> {
	
}
