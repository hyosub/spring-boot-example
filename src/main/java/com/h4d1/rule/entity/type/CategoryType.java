package com.h4d1.rule.entity.type;

public enum CategoryType {
	VISIT_COUNT(1, "VISIT_COUNT"),
	LIKE_COUNT(2, "LIKE_COUNT"),
	USER_RATING(3, "USER_RATING"),
	NEWEST_STORE_INFO(4, "NEWEST_STORE_INFO"),
	IS_ACTIVE_STORE_EVENT(5, "IS_ACTIVE_STORE_EVENT");
	
	private final int code;
	private final String name;
	
	private CategoryType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
