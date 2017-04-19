package com.h4d1.util;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class UuidGenerator {
	public String generateUUID() {
		//uuid 생성
		UUID uuid = UUID.randomUUID();
		//길이를 32자로 제한하기 위하여 '-'를 제거
		String uuidString = uuid.toString().replace("-", "");
		return uuidString;
	}
}
