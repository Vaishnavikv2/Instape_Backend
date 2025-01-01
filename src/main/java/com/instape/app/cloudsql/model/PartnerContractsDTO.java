package com.instape.app.cloudsql.model;

import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 31-Jul-2024
 * @ModifyDate - 31-Jul-2024
 * @Desc -
 */
public class PartnerContractsDTO {

	private String partnerContractId;

	private String partnerCode;

	private String ContractCode;

	private String contractName;

	private String status;

	private String deleted;

	private Timestamp createdDate;

	private String clientId;

	public String getPartnerContractId() {
		return partnerContractId;
	}

	public void setPartnerContractId(String partnerContractId) {
		this.partnerContractId = partnerContractId;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getContractCode() {
		return ContractCode;
	}

	public void setContractCode(String contractCode) {
		ContractCode = contractCode;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
