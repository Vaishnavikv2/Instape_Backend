package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.instape.app.cloudsql.model.EmployeeInfo;
import com.instape.app.cloudsql.repository.EmployeeInfoRepository;
import com.instape.app.cloudstore.dto.FirebaseTopicManagementResponse;
import com.instape.app.cloudstore.dto.NoSQLDocument;
import com.instape.app.cloudstore.service.CloudPubSubService;
import com.instape.app.firebase.service.FirestoreServiceHandler;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.FCMTokenResetService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.FirebaseScreen;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Nov-2023
 * @ModifyDate - 23-Nov-2023
 * @Desc - FCM Token Reset Service impl class
 */
@Service
public class FCMTokenResetServiceImpl implements FCMTokenResetService {

	static final Logger logger = LogManager.getFormatterLogger(FCMTokenResetServiceImpl.class);

	@Autowired
	private FirestoreServiceHandler firestoreServiceHandler;

	@Value("${FIRESTORE_ROOT}")
	private String firestoreRoot;

	@Autowired
	private CloudPubSubService cloudPubSubService;

	@Autowired
	private EmployeeInfoRepository employeeInfoRepository;

	@Override
	public CommonResponse resetFCMToken(String employerId, String employeeId, String userId) throws Exception {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		logger.info(String.format("Start of Reset FCM Token Service %s, %s", employerId, employeeId));
		NoSQLDocument notificationDocRef = firestoreServiceHandler.getDocRef(firestoreRoot, FirebaseScreen.INSTAPE,
				FirebaseScreen.EMPLOYERS, employerId, FirebaseScreen.EMPLOYEES, employeeId, FirebaseScreen.SCREENS,
				FirebaseScreen.NOTIFICATION);
		Map<String, Object> notificationDocRefData = notificationDocRef.getData();
		try {
			String customerCode = StringUtil.getEmployerCodeFromEmployerId(employerId);
			logger.info("Customer Code : " + customerCode);
			EmployeeInfo employee = employeeInfoRepository.getEmployeeInfoByEmployeeCodeAndCustomerCodeAndStatus(
					customerCode, employeeId, PortalConstant.INACTIVE);
			if (employee != null) {
				if (employee.getFcmToken() != null && !employee.getFcmToken().isEmpty()) {
					FirebaseTopicManagementResponse unSubscribeTopic = cloudPubSubService
							.unSubscribeTopic(employee.getFcmToken(), employerId);
					if (unSubscribeTopic.getSuccessCount() == 1) {
						logger.info("FCM Token Unsubscribe Success");
					} else {
						logger.info("FCM Token Unsubscribe Failed");
					}
					employee.setFcmToken(null);
					employee.setUpdatedBy(userId);
					employee.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					employeeInfoRepository.save(employee);
					if (notificationDocRefData != null && !notificationDocRefData.isEmpty()) {
						notificationDocRefData.put("device_fcm_token", "");
						notificationDocRefData.put("server_update", "");
						notificationDocRefData.put("timestamp",
								Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						notificationDocRef.update(notificationDocRefData);
					}
					response.setCode(PortalConstant.OK);
					response.setMessage(CommonMessageLog.FCMTOKENRESETSUCCESS);
					response.setStatus(CommonMessageLog.SUCCESS);
					stopWatch.stop();
					logger.info("Reset FCM Token Success");
					logger.info(String.format("Time taken on Reset FCM Token Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Reset FCM Token Service %s", CommonMessageLog.SUCCESS));
					return response;
				} else {
					response.setCode(PortalConstant.OK);
					response.setMessage(CommonMessageLog.FCMTOKENALREADYRESET);
					response.setStatus(CommonMessageLog.FAILED);
					stopWatch.stop();
					logger.info("FCM Token Alreday Reset");
					logger.info(String.format("Time taken on Reset FCM Token Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Reset FCM Token Service %s", CommonMessageLog.SUCCESS));
					return response;
				}
			} else {
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.EMPNOTFOUND);
				response.setStatus(CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Reset FCM Token failed");
				logger.info(String.format("Employee Not Found :- %s", employeeId));
				logger.info(
						String.format("Time taken on Reset FCM Token Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Reset FCM Token Service %s", CommonMessageLog.EMPNOTFOUND));
				return response;
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			response.setStatus(CommonMessageLog.FAILED);
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Reset FCM Token Service.", e);
			logger.info(String.format("Time taken on Reset FCM Token Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Reset FCM Token Service.");
			return response;
		}
	}

}
