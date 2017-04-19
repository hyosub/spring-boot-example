package com.h4d1.store.entity.type;

public enum StoreType {
	SMALL(1, "SMALL"),
	MIDIUM(2, "MIDIUM"),
	LARGE(3, "LARGE");
	
	private final int code;
	private final String name;
	
	private StoreType(int code, String name) {
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
