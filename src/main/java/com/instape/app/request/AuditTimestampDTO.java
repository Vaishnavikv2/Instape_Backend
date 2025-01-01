package com.instape.app.request;

import java.sql.Timestamp;

public class AuditTimestampDTO {

//	public Long getAuditId();
//	public Timestamp getUpdatedDate();
	private Long auditId;
	private Timestamp updatedDate;

	public AuditTimestampDTO(Long auditId, Timestamp updatedDate) {
		super();
		this.auditId = auditId;
		this.updatedDate = updatedDate;
	}

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

}
