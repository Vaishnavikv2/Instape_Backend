package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.instape.app.cloudsql.model.EmployeeInfo;
import com.instape.app.cloudsql.model.LenderRecord;
import com.instape.app.cloudsql.model.WorkflowRun;
import com.instape.app.cloudsql.repository.EmployeeInfoRepository;
import com.instape.app.cloudsql.repository.LenderRecordRepository;
import com.instape.app.cloudsql.repository.WorkflowRunRepository;
import com.instape.app.cloudstore.dto.NoSQLDocument;
import com.instape.app.firebase.service.FirestoreServiceHandler;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.OnboardingResetService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.FirebaseScreen;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 27-Nov-2023
 * @ModifyDate - 27-Nov-2023
 * @Desc - Onboard Reset Service impl class
 */
@Service
public class OnboardingResetServiceImpl implements OnboardingResetService {

	static final Logger logger = LogManager.getFormatterLogger(OnboardingResetServiceImpl.class);

	@Autowired
	private WorkflowRunRepository workflowRunRepository;

	@Autowired
	private LenderRecordRepository lenderRecordRepository;

	@Autowired
	private FirestoreServiceHandler firestoreServiceHandler;

	@Autowired
	private EmployeeInfoRepository employeeInfoRepository;

	@Value("${FIRESTORE_ROOT}")
	private String firestoreRoot;

	@Override
	public CommonResponse resetOnboarding(String employerId, String employeeId, String userId) throws Exception {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		logger.info(String.format("Start of Reset Onboarding Service %s, %s", employerId, employeeId));
		try {
			String customerCode = StringUtil.getEmployerCodeFromEmployerId(employerId);
			logger.info("Customer Code : " + customerCode);
			EmployeeInfo employeeInfo = employeeInfoRepository.getEmployeeInfoByEmployeeCodeAndCustomerCodeAndStatus(
					customerCode, employeeId, PortalConstant.ACTIVE, PortalConstant.Pending_For_Name_Consent);
			if (employeeInfo != null) {
				List<LenderRecord> lenderRecords = lenderRecordRepository
						.getLenderRecordsByCustomerCodeAndEmployeeCode(employerId, employeeId, PortalConstant.VALID);
				if (lenderRecords.isEmpty()) {
					List<WorkflowRun> workflowRuns = workflowRunRepository
							.getWorkflowRunsByCustomerCodeAndEmployeeCode(employerId, employeeId, PortalConstant.VALID);
					if (workflowRuns != null && !workflowRuns.isEmpty()) {
						workflowRuns.stream().forEach(wrkFlowRun -> wrkFlowRun.setRecordStatus(PortalConstant.INVALID));
						workflowRunRepository.saveAll(workflowRuns);
						logger.info("Workflow Run Records update : Success");
					}

					if (employeeInfo.getStatus().equals(PortalConstant.Pending_For_Name_Consent)) {
						employeeInfo.setStatus(PortalConstant.ACTIVE);
					}
					employeeInfo.setJourneyStatus(PortalConstant.PENDING);
					employeeInfo.setAadharHash(null);
					employeeInfo.setAadharSHA3(null);
					employeeInfo.setUpdatedBy(userId);
					employeeInfo.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					employeeInfoRepository.save(employeeInfo);
					logger.info("Employee Info update : Success");

					NoSQLDocument onboardingDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
							FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, employerId, FirebaseScreen.EMPLOYEES,
							employeeId, FirebaseScreen.SCREENS, FirebaseScreen.BANKONBOARDING);
					NoSQLDocument captureAddressDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
							FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, employerId, FirebaseScreen.EMPLOYEES,
							employeeId, FirebaseScreen.SCREENS, FirebaseScreen.CAPTUREADDRESS);
					NoSQLDocument profileDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
							FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, employerId, FirebaseScreen.EMPLOYEES,
							employeeId, FirebaseScreen.SCREENS, FirebaseScreen.PROFILE);
					Map<String, Object> onboardingDocRefData = onboardingDocRef.getData();
					Map<String, Object> captureAddressDocRefData = captureAddressDocRef.getData();
					Map<String, Object> profileDocRefData = profileDocRef.getData();
					if (onboardingDocRefData != null && !onboardingDocRefData.isEmpty()) {
						onboardingDocRefData.put("uid_pan_link_status", "");
						onboardingDocRefData.put("pan_status", "");
						onboardingDocRefData.put("face_status", "");
						onboardingDocRefData.put("aadhar_status", "");
						onboardingDocRefData.put("server_update", "");
						onboardingDocRefData.put("error_code", "");
						onboardingDocRefData.put("next_screen", "");
						onboardingDocRef.update(onboardingDocRefData);
						logger.info("Onboarding Doc Ref Data FS Documents update : Success");
					}
					if (captureAddressDocRefData != null && !captureAddressDocRefData.isEmpty()) {
						captureAddressDocRefData.put("selected_address_mode", "");
						captureAddressDocRefData.put("server_update", "");
						captureAddressDocRefData.put("next_screen", "");
						captureAddressDocRef.update(captureAddressDocRefData);
						logger.info("Capture Address Doc Ref Data FS Documents update : Success");
					}
					if (profileDocRefData != null && !profileDocRefData.isEmpty()) {
						profileDocRefData.put("photo", "");
						profileDocRefData.put("updated", "");
						profileDocRef.update(profileDocRefData);
						logger.info("Profile Doc Ref Data FS Documents update : Success");
					}

					response.setCode(PortalConstant.OK);
					response.setMessage(CommonMessageLog.ONBOARDINGRESETSUCCESS);
					response.setStatus(CommonMessageLog.SUCCESS);
					stopWatch.stop();
					logger.info("Reset Onboarding Success");
					logger.info(String.format("Time taken on Reset Onboarding Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Reset Onboarding Service %s", CommonMessageLog.SUCCESS));
					return response;
				} else {
					response.setCode(PortalConstant.OK);
					response.setMessage(CommonMessageLog.ONBOARDINGRESETNOTALLOWED);
					response.setStatus(CommonMessageLog.FAILED);
					stopWatch.stop();
					logger.info("Reset Onboarding failed");
					logger.info(String.format("Onboarding Reset Not Allowed :- %s", employeeId));
					logger.info(String.format("Time taken on Reset Onboarding Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Reset Onboarding Service %s", CommonMessageLog.EMPNOTFOUND));
					return response;
				}
			} else {
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.EMPNOTFOUND);
				response.setStatus(CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Employee Not Found : Reset Onboarding Failed");
				logger.info(String.format("Employee Not Found :- %s", employeeId));
				logger.info(
						String.format("Time taken on Reset Onboarding Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Reset Onboarding Service %s", CommonMessageLog.EMPNOTFOUND));
				return response;
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			response.setStatus(CommonMessageLog.FAILED);
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Reset Onboarding Service.", e);
			logger.info(String.format("Time taken on Reset Onboarding Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Reset Onboarding Service.");
			return response;
		}
	}
}
