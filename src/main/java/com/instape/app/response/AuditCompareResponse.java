package com.instape.app.response;

public class AuditCompareResponse {
	private AuditData left;
	private AuditData right;

	public AuditCompareResponse() {
		super();
	}

	public AuditCompareResponse(AuditData left, AuditData right) {
		super();
		this.left = left;
		this.right = right;
	}

	public AuditData getLeft() {
		return left;
	}

	public void setLeft(AuditData left) {
		this.left = left;
	}
	
	public void setLeft(String userName,Object data) {		
		this.left = new AuditData(userName, data);
	}

	public AuditData getRight() {
		return right;
	}

	public void setRight(AuditData right) {
		this.right = right;
	}
	
	public void setRight(String userName,Object data) {		
		this.right = new AuditData(userName, data);
	}

}
