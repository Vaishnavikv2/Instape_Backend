package com.instape.app.utils;

public enum UserStatus {
	ACTIVE("active"), INACTIVE("inactive"), DELETED("deleted"), BLOCKED("blocked");

	String value;

	private UserStatus(String val) {
		this.value = val;
	}
	
	public String getValue() {
		return value;
	}
}
