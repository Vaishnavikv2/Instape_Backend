package com.instape.app.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InternalImpsInqPayload {
	// inqtype will be inqByCif,inqByRrn, inqByLoanId
	private String inqType;
	private String contractCode;
	// for get loan status by rrn
	private String rrn;
	private String clientRefId;
	// for get all loan of cif
	private String cif;
	// for get loan status by loanId
	@JsonProperty("loanId")
	private String bankloanId;

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getBankloanId() {
		return bankloanId;
	}

	public void setBankloanId(String bankloanId) {
		this.bankloanId = bankloanId;
	}

	public String getInqType() {
		return inqType;
	}

	public void setInqType(String inqType) {
		this.inqType = inqType;
	}

	public String getClientRefId() {
		return clientRefId;
	}

	public void setClientRefId(String clientRefId) {
		this.clientRefId = clientRefId;
	}	

}
