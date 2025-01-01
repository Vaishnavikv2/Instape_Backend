package com.instape.app.cloudsql.model;

import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Aug-2024
 * @ModifyDate - 07-Aug-2024
 * @Desc -
 */
public class DemandRecordDTO {

	private String id;

	private String bankDemandId;

	private String totalLoanAmount;

	private String totalInterestAmount;

	private String total;

	private String matchStatus;

	private Timestamp createdDate;

	public DemandRecordDTO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankDemandId() {
		return bankDemandId;
	}

	public void setBankDemandId(String bankDemandId) {
		this.bankDemandId = bankDemandId;
	}

	public String getTotalLoanAmount() {
		return totalLoanAmount;
	}

	public void setTotalLoanAmount(String totalLoanAmount) {
		this.totalLoanAmount = totalLoanAmount;
	}

	public String getTotalInterestAmount() {
		return totalInterestAmount;
	}

	public void setTotalInterestAmount(String totalInterestAmount) {
		this.totalInterestAmount = totalInterestAmount;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(String matchStatus) {
		this.matchStatus = matchStatus;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
}
