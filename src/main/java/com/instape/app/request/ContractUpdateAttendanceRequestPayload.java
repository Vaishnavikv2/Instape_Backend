package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 08-Aug-2024
 * @ModifyDate - 08-Aug-2024
 * @Desc -
 */
public class ContractUpdateAttendanceRequestPayload {

	private String contractCode;

	private String userId;

	private String attendanceUnit;

	private String hrsInDay;

	private String noticePeriodThreshold;

	private String daysInMonth;

	private String requiredHours;

	private String sameLocationOnly;

	private String sameClientOnly;

	private String primaryClientOnly;

	private String limitedClientOnly;

	private String limitedLocationOnly;

	private String countAttendancePrimaryClientOnly;

	private String enforceRulesForPunch;

	private String attendanceMaxHrs;

	private String attendanceMinHrs;

	private String radius;

	private String liveness;

	private String serverTimestamp;

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

	public String getAttendanceUnit() {
		return attendanceUnit;
	}

	public void setAttendanceUnit(String attendanceUnit) {
		this.attendanceUnit = attendanceUnit;
	}

	public String getHrsInDay() {
		return hrsInDay;
	}

	public void setHrsInDay(String hrsInDay) {
		this.hrsInDay = hrsInDay;
	}

	public String getNoticePeriodThreshold() {
		return noticePeriodThreshold;
	}

	public void setNoticePeriodThreshold(String noticePeriodThreshold) {
		this.noticePeriodThreshold = noticePeriodThreshold;
	}

	public String getDaysInMonth() {
		return daysInMonth;
	}

	public void setDaysInMonth(String daysInMonth) {
		this.daysInMonth = daysInMonth;
	}

	public String getRequiredHours() {
		return requiredHours;
	}

	public void setRequiredHours(String requiredHours) {
		this.requiredHours = requiredHours;
	}

	public String getSameLocationOnly() {
		return sameLocationOnly;
	}

	public void setSameLocationOnly(String sameLocationOnly) {
		this.sameLocationOnly = sameLocationOnly;
	}

	public String getSameClientOnly() {
		return sameClientOnly;
	}

	public void setSameClientOnly(String sameClientOnly) {
		this.sameClientOnly = sameClientOnly;
	}

	public String getPrimaryClientOnly() {
		return primaryClientOnly;
	}

	public void setPrimaryClientOnly(String primaryClientOnly) {
		this.primaryClientOnly = primaryClientOnly;
	}

	public String getLimitedClientOnly() {
		return limitedClientOnly;
	}

	public void setLimitedClientOnly(String limitedClientOnly) {
		this.limitedClientOnly = limitedClientOnly;
	}

	public String getLimitedLocationOnly() {
		return limitedLocationOnly;
	}

	public void setLimitedLocationOnly(String limitedLocationOnly) {
		this.limitedLocationOnly = limitedLocationOnly;
	}

	public String getCountAttendancePrimaryClientOnly() {
		return countAttendancePrimaryClientOnly;
	}

	public void setCountAttendancePrimaryClientOnly(String countAttendancePrimaryClientOnly) {
		this.countAttendancePrimaryClientOnly = countAttendancePrimaryClientOnly;
	}

	public String getEnforceRulesForPunch() {
		return enforceRulesForPunch;
	}

	public void setEnforceRulesForPunch(String enforceRulesForPunch) {
		this.enforceRulesForPunch = enforceRulesForPunch;
	}

	public String getAttendanceMaxHrs() {
		return attendanceMaxHrs;
	}

	public void setAttendanceMaxHrs(String attendanceMaxHrs) {
		this.attendanceMaxHrs = attendanceMaxHrs;
	}

	public String getAttendanceMinHrs() {
		return attendanceMinHrs;
	}

	public void setAttendanceMinHrs(String attendanceMinHrs) {
		this.attendanceMinHrs = attendanceMinHrs;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getLiveness() {
		return liveness;
	}

	public void setLiveness(String liveness) {
		this.liveness = liveness;
	}

	public String getServerTimestamp() {
		return serverTimestamp;
	}

	public void setServerTimestamp(String serverTimestamp) {
		this.serverTimestamp = serverTimestamp;
	}
}
