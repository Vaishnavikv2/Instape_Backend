package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 08-Aug-2024
 * @ModifyDate - 08-Aug-2024
 * @Desc -
 */
public class ContractUpdateServicesRequestPayload {

	private String contractCode;

	private String userId;

	private String attendanceServiceStatus;

	private String advanceServiceStatus;

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAttendanceServiceStatus() {
		return attendanceServiceStatus;
	}

	public void setAttendanceServiceStatus(String attendanceServiceStatus) {
		this.attendanceServiceStatus = attendanceServiceStatus;
	}

	public String getAdvanceServiceStatus() {
		return advanceServiceStatus;
	}

	public void setAdvanceServiceStatus(String advanceServiceStatus) {
		this.advanceServiceStatus = advanceServiceStatus;
	}
}
