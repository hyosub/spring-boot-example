package com.h4d1.rule.entity.type;

public enum ValueType {
	TWO_COND_BOOL_AND_VALUE_DESC(1, "TWO_COND_BOOL_AND_VALUE_DESC"),
	TWO_COND_BOOL_AND_VALUE_ASC(2, "TWO_COND_BOOL_AND_VALUE_ASC"),
	THREE_COND_NUMERIC_AND_VALUE_ASC(3, "THREE_COND_NUMERIC_AND_VALUE_ASC"),
	THREE_COND_NUMERIC_AND_VALUE_DESC(4, "THREE_COND_NUMERIC_AND_VALUE_DESC"),
	THREE_COND_DATE_AND_VALUE_ASC(5, "THREE_COND_DATE_AND_VALUE_ASC"),
	THREE_COND_DATE_AND_VALUE_DESC(6, "THREE_COND_DATE_AND_VALUE_DESC");
	
	private final int code;
	private final String name;
	
	private ValueType(int code, String name) {
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
