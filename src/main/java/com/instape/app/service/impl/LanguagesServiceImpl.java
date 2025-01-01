package com.instape.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.instape.app.cloudsql.model.RemoteConfigAuditLogs;
import com.instape.app.cloudstore.service.RemoteConfigService;
import com.instape.app.request.TemplateRequestPayload;
import com.instape.app.service.AuditService;
import com.instape.app.service.LanguagesService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.FirebaseScreen;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 18-Sep-2024
 * @ModifyDate - 18-Sep-2024
 * @Desc -
 */
@Service
public class LanguagesServiceImpl implements LanguagesService {

	static final Logger logger = LogManager.getFormatterLogger(LanguagesServiceImpl.class);

	@Autowired
	private RemoteConfigService remoteConfigService;

	@Autowired
	private AuditService auditService;

	@Override
	public Map<Object, Object> getTemplate() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info("Start of get Template Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			String templateJson = remoteConfigService.getTemplateJson(FirebaseScreen.TRANSLATIONS);
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			responseMap.put("data", templateJson);
			stopWatch.stop();
			logger.info("Get Template Service Success");
			logger.info(String.format("Time taken on get Template Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of get Template Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Template Service.", e);
			logger.info(String.format("Time taken on get Template Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Template Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> publishTemplate(TemplateRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Publish Template Service");
			Map<String, Object> templates = payload.getTemplates();
			Set<Entry<String, Object>> entrySet = templates.entrySet();
			Map<String, String> publishTemplates = new HashMap<String, String>();
			for (Entry<String, Object> entry : entrySet) {
				publishTemplates.put(entry.getKey(), StringUtil.pojoToString(entry.getValue()));
			}
			boolean publishSuccess = remoteConfigService.publishTemplateJson(publishTemplates);
			if (!publishSuccess) {
				responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("message", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Publish Template Failed");
				logger.info(
						String.format("Time taken on Publish Template Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Publish Template Service %s", CommonMessageLog.FAILED));
				return responseMap;
			}
			logger.info("Publish Template in Remote Config Success");

			for (Entry<String, Object> entry : entrySet) {
				RemoteConfigAuditLogs auditLog = new RemoteConfigAuditLogs();
				auditLog.setKey(entry.getKey());
				auditLog.setReConfigJson(entry.getValue());
				auditLog.setUpdatedBy(payload.getUserId());
				auditLog.setUpdatedDate(DateUtils.getCurrentTimestamp());
				auditService.saveAuditLogs(auditLog);
			}

			logger.info("Audit Log Saved");

			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info("Publish Template Success");
			logger.info(String.format("Time taken on Publish Template Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Publish Template Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Publish Template Service.", e);
			logger.info(String.format("Time taken on Publish Template Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Publish Template Service.");
			return responseMap;
		}
	}
}
