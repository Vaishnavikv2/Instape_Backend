package com.instape.app.request;

public class EmailRequestPayload {
	
	private String email;
	private String otp;
	private String employeeName;

	
	public EmailRequestPayload(String name,String email, String otp) {
		super();
		this.email = email;
		this.otp = otp;
		this.employeeName=name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}
