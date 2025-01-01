package com.instape.app.cloudsql.model;

import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 20-Aug-2024
 * @ModifyDate - 20-Aug-2024
 * @Desc -
 */
public class ContractAuditTimestampDTO {

	private Long contractId;

	private Timestamp timestamp;

	public ContractAuditTimestampDTO() {
		super();
	}

	public ContractAuditTimestampDTO(Long contractId, Timestamp timestamp) {
		super();
		this.contractId = contractId;
		this.timestamp = timestamp;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}
