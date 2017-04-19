package com.h4d1.exception.type;

public enum ServiceError {
	BAD_REQUEST(400, "Bad request."),
	NOT_FOUND(404, "Not found."),
	INVALID_REFLECTION_RATE_TOTAL(1000, "Invalid a total of reflection rate. A total of reflection rate must be 100."),
	IS_NOT_EQUAL_REFLECTION_RATE_TOTAL_AND_VIEWPOINT_RULE_REFLECTION_RATE_TOTAL(1001, "Reflection-rate-total of rule and Reflection-rate-total of viewpoint-rule is not equal."),
	IS_NOT_EQUAL_REFLECTION_RATE_TOTAL_AND_CATEGORY_RULE_REFLECTION_RATE_TOTAL(1002, "Reflection-rate-total of rule and Reflection-rate-total of category-rule is not equal."),
	INVALID_VIEWPOINT_TYPE(1003, "Invalid viewpoint type."),
	INVALID_CATEGORY_TYPE(1004, "Invalid category type."),
	INVALID_VALUE_TYPE(1005, "Invalid value type."),
	INVALID_FIRST_CONDITION(1006, "Invalid first condition of value-rule."),
	INVALID_SECOND_CONDITION(1007, "Invalid second condition of value-rule."),
	INVALID_THIRD_CONDITION(1008, "Invalid third condition of value-rule."),
	INVALID_CONDITIONS_RANGE(1009, "Invalid interval of conditions."),
	INVALID_VALUE_ORDER(1010, "Invalid order of value."),
	UNKNOWN_ERROR(9999, "Unknown error.");
	
	private final int code;
	private final String message;
	
	private ServiceError(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
