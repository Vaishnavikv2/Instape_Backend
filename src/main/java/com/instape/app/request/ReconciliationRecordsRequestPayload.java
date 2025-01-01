package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Aug-2024
 * @ModifyDate - 07-Aug-2024
 * @Desc -
 */
public class ReconciliationRecordsRequestPayload {

	private String reconStatus;

	private String contractCode;

	private String mismatchType;

	private String demandId;

	public String getReconStatus() {
		return reconStatus;
	}

	public void setReconStatus(String reconStatus) {
		this.reconStatus = reconStatus;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getMismatchType() {
		return mismatchType;
	}

	public void setMismatchType(String mismatchType) {
		this.mismatchType = mismatchType;
	}

	public String getDemandId() {
		return demandId;
	}

	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}
}
