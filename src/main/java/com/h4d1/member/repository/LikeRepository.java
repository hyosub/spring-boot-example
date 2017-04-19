package com.h4d1.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.h4d1.member.entity.Like;

public interface LikeRepository extends JpaRepository<Like, String> {
	@Query("select l from Like l where l.member.id = :memberId and l.store.id = :storeId")
	Like findByMemberIdAndStoreId(@Param("memberId") String memberId, @Param("storeId") String storeId);
}
