package com.h4d1.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.h4d1.member.entity.UserRating;

public interface UserRatingRepository extends JpaRepository<UserRating, String> {
	@Query("select ur from UserRating ur where ur.member.id = :memberId and ur.store.id = :storeId")
	UserRating findByMemberIdAndStoreId(@Param("memberId") String memberId, @Param("storeId") String storeId);
	
	@Query("select ur from UserRating ur where ur.store.id = :storeId")
	List<UserRating> findByStoreId(@Param("storeId") String storeId);
}
