package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.instape.app.cloudsql.model.BankMaster;
import com.instape.app.cloudsql.repository.BankMasterRepository;
import com.instape.app.request.BankMasterRequestPayload;
import com.instape.app.service.AuditService;
import com.instape.app.service.BankService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 02-Jul-2024
 * @ModifyDate - 02-Jul-2024
 * @Desc -
 */
@Service
public class BankServiceImpl implements BankService {

	static final Logger logger = LogManager.getFormatterLogger(BankServiceImpl.class);

	@Autowired
	private BankMasterRepository bankMasterRepository;
	
	@Autowired
	private AuditService auditService;

	@Override
	public Map<Object, Object> createBank(BankMasterRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Create Bank Master Service");
			BankMaster bankMasterExist = bankMasterRepository.findByCode(payload.getBankCode());

			if (bankMasterExist == null) {

				BankMaster bankMaster = new BankMaster();
				bankMaster.setBankLogoPath(payload.getBankLogoPth());
				bankMaster.setBankName(payload.getBankName());
				bankMaster.setCinNumber(payload.getCinNumber());
				bankMaster.setCode(payload.getBankCode());
				bankMaster.setContactBankAddress(payload.getContactBankAddress());
				bankMaster.setContactBankEmail(payload.getContactBankEmail());
				bankMaster.setContactBankPhone(payload.getContactBankPhone());
				bankMaster.setGstNumber(payload.getGstNumber());
				bankMaster.setWebsite(payload.getWebsite());
				bankMaster.setCreatedBy(payload.getUserId());
				bankMaster.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				bankMaster.setUpdatedBy(payload.getUserId());
				bankMaster.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				bankMaster.setStatus(PortalConstant.ACTIVE);

				bankMasterRepository.save(bankMaster);
				auditService.saveAuditLogs(bankMaster);
				
				logger.info("Creation of Bank Master in DB Success");
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Create Bank Master Success");
				logger.info(String.format("Time taken on Create Bank Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Create Bank Master Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.BANKCODEALREADYEXIST);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Create Bank Master failed");
				logger.info(String.format("Bank Master Code Already Exist :-"));
				logger.info(String.format("Time taken on Create Bank Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Create Bank Master Service %s", CommonMessageLog.CONTRACTALREADYEXIST));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Create Bank Master Service.", e);
			logger.info(
					String.format("Time taken on Create Bank Master Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Bank Master Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateBank(BankMasterRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Bank Master Service");

			BankMaster bankMasterExist = bankMasterRepository.findById(Long.parseLong(payload.getId())).orElse(null);

			if (bankMasterExist != null) {

				BankMaster bankMaster = bankMasterExist;
				bankMaster.setBankLogoPath(payload.getBankLogoPth());
				bankMaster.setBankName(payload.getBankName());
				bankMaster.setCinNumber(payload.getCinNumber());
				bankMaster.setContactBankAddress(payload.getContactBankAddress());
				bankMaster.setContactBankEmail(payload.getContactBankEmail());
				bankMaster.setContactBankPhone(payload.getContactBankPhone());
				bankMaster.setGstNumber(payload.getGstNumber());
				bankMaster.setWebsite(payload.getWebsite());
				bankMaster.setUpdatedBy(payload.getUserId());
				bankMaster.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));

				bankMasterRepository.save(bankMaster);
				auditService.saveAuditLogs(bankMaster);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Bank Master Success");
				logger.info(String.format("Time taken on Update Bank Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Bank Master Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Bank Master failed");
				logger.info(String.format("Bank Master Not Found :-"));
				logger.info(String.format("Time taken on Update Bank Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Bank Master Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Bank Master Service.", e);
			logger.info(
					String.format("Time taken on Update Bank Master Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Bank Master Service.");
			return responseMap;
		}
	}
}
