package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Oct-2024
 * @ModifyDate - 07-Oct-2024
 * @Desc -
 */
public class UpdateLoanStatusRequestPayload {

	private String bankLoanId;

	private String loanStatus;

	private String userId;

	public String getBankLoanId() {
		return bankLoanId;
	}

	public void setBankLoanId(String bankLoanId) {
		this.bankLoanId = bankLoanId;
	}

	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
