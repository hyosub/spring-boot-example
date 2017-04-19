package com.h4d1.rule.entity.type;

public enum ViewpointType {
	STORE(1, "STORE"),
	CUSTOMER(2, "CUSTOMER"),
	USER_CUSTOM(3, "USER_CUSTOM");
	
	private final int code;
	private final String name;
	
	private ViewpointType(int code, String name) {
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
