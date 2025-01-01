package com.instape.app.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.instape.app.cloudsql.model.PartnerApiTestCaseExecution;
import com.instape.app.cloudsql.repository.PartnerApiTestCaseExecutionRepository;
import com.instape.app.request.UpdateTestCaseExecutionRequestPayload;
import com.instape.app.service.InternalRestService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Sep-2024
 * @ModifyDate - 04-Sep-2024
 * @Desc -
 */
@Service
public class InternalRestServiceImpl implements InternalRestService {

	static final Logger logger = LogManager.getFormatterLogger(InternalRestServiceImpl.class);

	@Autowired
	private PartnerApiTestCaseExecutionRepository partnerApiTestCaseExecutionRepository;

	@Override
	public Map<Object, Object> updateTestCaseExecution(UpdateTestCaseExecutionRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Update Partner APIs Test Case Execute Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			PartnerApiTestCaseExecution testCaseExecutionExist = partnerApiTestCaseExecutionRepository
					.findByUUID(payload.getUuid());
			if (testCaseExecutionExist != null) {
				logger.info("Test Case Execution Exist");
				testCaseExecutionExist.setUpdatedDate(DateUtils.getCurrentTimestamp());
				testCaseExecutionExist.setStatus(payload.getStatus());
				testCaseExecutionExist.setLogPath(payload.getLogPath());
				testCaseExecutionExist.setRespPath(payload.getRespPath());
				partnerApiTestCaseExecutionRepository.save(testCaseExecutionExist);
				logger.info("Test Case Execution Updated in DB Successfully");

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Partner APIs Test Case Execute Service Success");
				logger.info(String.format("Time taken on Update Partner APIs Test Case Execute Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Partner APIs Test Case Execute Service %s",
						CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Partner APIs Test Case Execute Service failed");
				logger.info("Partner API Test CaseExecution Record Not Found :- ");
				logger.info(String.format("Time taken on Update Partner APIs Test Case Execute Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Partner APIs Test Case Execute Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Partner APIs Test Case Execute Service.", e);
			logger.info(String.format("Time taken on Update Partner APIs Test Case Execute Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner APIs Test Case Execute Service.");
			return responseMap;
		}
	}
}
