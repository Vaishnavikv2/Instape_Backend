package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instape.app.cloudsql.model.BankAuditLogs;
import com.instape.app.cloudsql.model.BankMaster;
import com.instape.app.cloudsql.model.CustomerAuditLogs;
import com.instape.app.cloudsql.model.CustomerMaster;
import com.instape.app.cloudsql.model.LenderEndpointsConfig;
import com.instape.app.cloudsql.model.LenderEndpointsConfigAuditLogs;
import com.instape.app.cloudsql.model.LoanRecord;
import com.instape.app.cloudsql.model.LoanRecordAuditLogs;
import com.instape.app.cloudsql.model.RemoteConfig;
import com.instape.app.cloudsql.model.RemoteConfigAuditLogs;
import com.instape.app.cloudsql.model.SPLoginAudit;
import com.instape.app.cloudsql.model.Users;
import com.instape.app.cloudsql.model.UsersAuditLogs;
import com.instape.app.cloudsql.repository.LoanRecordAuditLogsRepositry;
import com.instape.app.cloudsql.repository.RemoteConfigAuditLogsRepository;
import com.instape.app.cloudsql.repository.SPLoginAuditRepository;
import com.instape.app.cloudsql.repository.UserRepository;
import com.instape.app.cloudsql.repository.audit.BankAuditRepository;
import com.instape.app.cloudsql.repository.audit.CustomerAuditRepository;
import com.instape.app.cloudsql.repository.audit.LenderEndpointAuditRepository;
import com.instape.app.cloudsql.repository.audit.UsersAuditRepository;
import com.instape.app.request.AuditCompareRequestDTO;
import com.instape.app.request.AuditTimestampDTO;
import com.instape.app.response.AuditCompareResponse;
import com.instape.app.service.AuditService;

@Service
public class AuditServiceImpl implements AuditService {
	private static final Logger logger = LogManager.getLogger(AuditServiceImpl.class);

	@Autowired
	private SPLoginAuditRepository loginAuditRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CustomerAuditRepository customerAuditRepository;

	@Autowired
	private BankAuditRepository bankAuditRepository;

	@Autowired
	private LenderEndpointAuditRepository lenderEndpointAuditRepository;

	@Autowired
	private UsersAuditRepository userAuditRepository;

	@Autowired
	private RemoteConfigAuditLogsRepository remoteConfigAuditLogsRepository;

	@Autowired
	private LoanRecordAuditLogsRepositry loanRecordAuditLogsRepositry;

	@Override
	public void saveLoginSuccessAudit(Users user) {
		CompletableFuture.runAsync(() -> saveLoginAudit(user, "Login", "success", null));
	}

	@Override
	public void saveLoginFailedAudit(Users user, int leftAttempt) {
		CompletableFuture.runAsync(() -> saveLoginAudit(user, "Login", "failed", leftAttempt));
	}

	@Override
	public void saveLogoutAudit(Users user) {
		CompletableFuture.runAsync(() -> saveLoginAudit(user, "Logout", "success", null));
	}

	private void saveLoginAudit(Users user, String activity, String status, Integer leftAttempt) {
		logger.info("Login audit -> User:{} Activity: {}, status:{}", user.getEmail(), activity, status);
		SPLoginAudit audit = new SPLoginAudit();
		audit.setUsers(user);
		audit.setUserEmail(user.getEmail());
		audit.setActivity(activity);
		audit.setLeftAttempt(leftAttempt);
		audit.setStatus(status);
		audit.setActivityTime(Timestamp.valueOf(LocalDateTime.now()));
		loginAuditRepository.save(audit);
		logger.info("Audit data saved");
	}

	@Override
	public void saveAuditLogs(Object auditData) {
		CompletableFuture.runAsync(() -> saveAudit(auditData));
	}

	private void saveAudit(Object auditData) {
		logger.info("Going to save Audit data");
		if (auditData instanceof CustomerMaster) {
			CustomerAuditLogs customerAuditLog = mapper.map(auditData, CustomerAuditLogs.class);
			customerAuditRepository.save(customerAuditLog);
			logger.info("Customer Audit data saved");
		} else if (auditData instanceof BankMaster) {
			BankAuditLogs bankAuditLog = mapper.map(auditData, BankAuditLogs.class);
			bankAuditRepository.save(bankAuditLog);
			logger.info("Bank Audit data saved");
		} else if (auditData instanceof LenderEndpointsConfig) {
			LenderEndpointsConfigAuditLogs auditLog = mapper.map(auditData, LenderEndpointsConfigAuditLogs.class);
			lenderEndpointAuditRepository.save(auditLog);
			logger.info("Lender Endpoint Audit data saved");
		} else if (auditData instanceof RemoteConfig) {
			RemoteConfigAuditLogs auditLog = mapper.map(auditData, RemoteConfigAuditLogs.class);
			remoteConfigAuditLogsRepository.save(auditLog);
			logger.info("Remote Config Audit data saved");
		} else if (auditData instanceof Users) {
			UsersAuditLogs auditLog = mapper.map(auditData, UsersAuditLogs.class);
			userAuditRepository.save(auditLog);
			logger.info("User Audit data saved");
		} else if (auditData instanceof LoanRecord) {
			LoanRecordAuditLogs auditLog = mapper.map(auditData, LoanRecordAuditLogs.class);
			loanRecordAuditLogsRepositry.save(auditLog);
			logger.info("Loan Record Audit data saved");
		}

		logger.info("Saving Audit data done");
	}

	@Override
	public List<AuditTimestampDTO> getAuditTimestamp(String resouceName, Long resouceId) {
		List<AuditTimestampDTO> timestamps = new ArrayList<>();
		switch (resouceName) {
		case "bank":
			List<BankAuditLogs> bankAudit = bankAuditRepository.getAllAuditLogsById(resouceId);
			timestamps = bankAudit.stream().map(a -> new AuditTimestampDTO(a.getAuditId(), a.getUpdatedDate()))
					.toList();
			break;
		case "customer":
			List<CustomerAuditLogs> customerAudit = customerAuditRepository.getAllAuditLogsById(resouceId);
			timestamps = customerAudit.stream().map(a -> new AuditTimestampDTO(a.getAuditId(), a.getUpdatedDate()))
					.toList();
			break;
		case "customer_user":
			break;
		case "lender":

			break;
		case "resource":

			break;
		case "resource_category":

			break;
		case "permission":

			break;
		case "product":

			break;
		case "role":

			break;
		case "user":
			List<UsersAuditLogs> usersAudit = userAuditRepository.getAllAuditLogsById(resouceId);
			timestamps = usersAudit.stream().map(a -> new AuditTimestampDTO(a.getAuditId(), a.getUpdatedDate()))
					.toList();
			break;
		case "lender_endpoint":
			List<LenderEndpointsConfigAuditLogs> lenderEndpointAudit = lenderEndpointAuditRepository
					.getAllAuditLogsById(resouceId);
			timestamps = lenderEndpointAudit.stream()
					.map(a -> new AuditTimestampDTO(a.getAuditId(), a.getUpdatedDate())).toList();
			break;
		case "loan_record":
			List<LoanRecordAuditLogs> loanRecordAudit = loanRecordAuditLogsRepositry.getAllAuditLogsById(resouceId);
			timestamps = loanRecordAudit.stream().map(a -> new AuditTimestampDTO(a.getAuditId(), a.getUpdatedDate()))
					.toList();
			break;
		}
		return timestamps;
	}

	@Override
	public AuditCompareResponse getAuditCompare(AuditCompareRequestDTO payload) {
		AuditCompareResponse response = new AuditCompareResponse();
		switch (payload.getResourceName()) {
		case "bank":
			response = getBankAuditCompare(payload);
			break;
		case "customer":
			response = getCustomerAuditCompare(payload);
			break;
		case "lender_endpoint":
			response = getLenderEndpointAuditCompare(payload);
			break;
		case "loan_record":
			response = getLoanRecordAuditCompare(payload);
			break;
		case "customer_user":
			break;
		case "lender":
			break;
		case "resource":
			break;
		case "resource_category":
			break;
		case "permission":
			break;
		case "product":
			break;
		case "role":
			break;
		case "user":
			break;
		}
		return response;
	}

	private AuditCompareResponse getBankAuditCompare(AuditCompareRequestDTO payload) {
		AuditCompareResponse response = new AuditCompareResponse();
		BankAuditLogs leftRecord = bankAuditRepository.getAuditLogByAuditId(payload.getLeftId());
		BankAuditLogs rightRecord = bankAuditRepository.getAuditLogByAuditId(payload.getRightId());
		response.setLeft(getUserName(leftRecord.getUpdatedBy()), leftRecord);
		response.setRight(getUserName(rightRecord.getUpdatedBy()), rightRecord);
		return response;
	}

	private AuditCompareResponse getCustomerAuditCompare(AuditCompareRequestDTO payload) {
		AuditCompareResponse response = new AuditCompareResponse();
		CustomerAuditLogs leftRecord = customerAuditRepository.getAuditLogByAuditId(payload.getLeftId());
		CustomerAuditLogs rightRecord = customerAuditRepository.getAuditLogByAuditId(payload.getRightId());
		response.setLeft(getUserName(leftRecord.getUpdatedBy()), leftRecord);
		response.setRight(getUserName(rightRecord.getUpdatedBy()), rightRecord);
		return response;
	}

	private AuditCompareResponse getLenderEndpointAuditCompare(AuditCompareRequestDTO payload) {
		AuditCompareResponse response = new AuditCompareResponse();
		LenderEndpointsConfigAuditLogs leftRecord = lenderEndpointAuditRepository
				.getAuditLogByAuditId(payload.getLeftId());
		LenderEndpointsConfigAuditLogs rightRecord = lenderEndpointAuditRepository
				.getAuditLogByAuditId(payload.getRightId());
		response.setLeft(getUserName(leftRecord.getUpdatedBy()), leftRecord);
		response.setRight(getUserName(rightRecord.getUpdatedBy()), rightRecord);
		return response;
	}

	private AuditCompareResponse getLoanRecordAuditCompare(AuditCompareRequestDTO payload) {
		AuditCompareResponse response = new AuditCompareResponse();
		LoanRecordAuditLogs leftRecord = loanRecordAuditLogsRepositry.getAuditLogByAuditId(payload.getLeftId());
		LoanRecordAuditLogs rightRecord = loanRecordAuditLogsRepositry.getAuditLogByAuditId(payload.getRightId());
		response.setLeft(getUserName(leftRecord.getUpdatedBy()), leftRecord);
		response.setRight(getUserName(rightRecord.getUpdatedBy()), rightRecord);
		return response;
	}

	private String getUserName(String userId) {
		return userId != null ? userRepository.getUsersNameById(Long.parseLong(userId)) : "";
	}

}
