package com.h4d1.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h4d1.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

}
