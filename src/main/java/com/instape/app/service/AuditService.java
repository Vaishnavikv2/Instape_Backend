package com.instape.app.service;

import java.util.List;

import com.instape.app.cloudsql.model.Users;
import com.instape.app.request.AuditCompareRequestDTO;
import com.instape.app.request.AuditTimestampDTO;
import com.instape.app.response.AuditCompareResponse;

public interface AuditService {
	void saveLoginSuccessAudit(Users user);
	void saveLoginFailedAudit(Users user,int leftAttempt);
	void saveLogoutAudit(Users user);
	void saveAuditLogs(Object auditData);
	List<AuditTimestampDTO> getAuditTimestamp(String resouceName,Long resouceId);
	AuditCompareResponse getAuditCompare(AuditCompareRequestDTO payload);
}
