package com.h4d1.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h4d1.store.entity.StoreAddress;

public interface StoreAddressRepository extends JpaRepository<StoreAddress, String> {

}
