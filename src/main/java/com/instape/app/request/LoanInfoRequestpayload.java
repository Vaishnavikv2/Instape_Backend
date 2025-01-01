package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 15-Dec-2023
 * @ModifyDate - 15-Dec-2023
 * @Desc -
 */
public class LoanInfoRequestpayload {

	private String loanId;

	public LoanInfoRequestpayload(String loanId) {
		super();
		this.loanId = loanId;
	}

	public LoanInfoRequestpayload() {
		super();
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
}
