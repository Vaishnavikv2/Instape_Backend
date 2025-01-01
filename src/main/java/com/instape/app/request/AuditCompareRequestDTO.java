package com.instape.app.request;

public class AuditCompareRequestDTO {
	String resourceName;
	Long resourceId;
	Long leftId;
	Long rightId;

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public Long getLeftId() {
		return leftId;
	}

	public void setLeftId(Long leftId) {
		this.leftId = leftId;
	}

	public Long getRightId() {
		return rightId;
	}

	public void setRightId(Long rightId) {
		this.rightId = rightId;
	}

}
