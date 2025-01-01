package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.instape.app.cloudsql.model.OptionsMaster;
import com.instape.app.cloudsql.repository.OptionsMasterRepository;
import com.instape.app.request.OptionMasterRequestPayload;
import com.instape.app.service.OptionMasterService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Aug-2024
 * @ModifyDate - 23-Aug-2024
 * @Desc -
 */
@Service
public class OptionMasterServiceImpl implements OptionMasterService {

	static final Logger logger = LogManager.getFormatterLogger(OptionMasterServiceImpl.class);

	@Autowired
	private OptionsMasterRepository optionsMasterRepository;

	@Override
	public Map<Object, Object> addOptionMaster(OptionMasterRequestPayload payload, Long userID) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Add Option Master Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			OptionsMaster optionMasterExist = optionsMasterRepository
					.findByOptionMasterNameAndStatus(payload.getOptionType(), PortalConstant.ACTIVE);
			if (optionMasterExist == null) {
				OptionsMaster optionsMaster = new OptionsMaster();
				optionsMaster.setCreatedBy(userID.toString());
				optionsMaster.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				optionsMaster.setUpdatedBy(userID.toString());
				optionsMaster.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				optionsMaster.setOptionDesc(payload.getOptionDesc());
				optionsMaster.setOptionName(payload.getOptionName());
				optionsMaster.setOptionType(payload.getOptionType());
				optionsMaster.setOptionValue(payload.getOptionValue());
				optionsMaster.setStatus(PortalConstant.ACTIVE);
				optionsMasterRepository.save(optionsMaster);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Add Option Master Service Success");
				logger.info(String.format("Time taken on Add Option Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Option Master Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.OptionMasterAlreadyExist);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Add Option Master Service failed");
				logger.info("Option Master Already Exist :- ");
				logger.info(String.format("Time taken on Add Option Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Add Option Master Service %s", CommonMessageLog.ConstantAlreadyExist));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add Option Master Service.", e);
			logger.info(
					String.format("Time taken on Add Option Master Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Option Master Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getOptionMsters(PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Option Master Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<OptionsMaster> optionsMasters = optionsMasterRepository.findByStatus(PortalConstant.ACTIVE,
					pageRequest);
			if (optionsMasters != null && !optionsMasters.isEmpty()) {
				logger.info("Option Masters Recods Exist");
				List<OptionMasterRequestPayload> optionsMastersDTOs = new ArrayList<OptionMasterRequestPayload>();
				for (OptionsMaster optionsMaster : optionsMasters) {
					OptionMasterRequestPayload optionMasterRequestPayload = new OptionMasterRequestPayload();
					optionMasterRequestPayload.setId(optionsMaster.getId().toString());
					optionMasterRequestPayload.setOptionDesc(optionsMaster.getOptionDesc());
					optionMasterRequestPayload.setOptionName(optionsMaster.getOptionName());
					optionMasterRequestPayload.setOptionType(optionsMaster.getOptionType());
					optionMasterRequestPayload.setOptionValue(optionsMaster.getOptionValue());
					optionMasterRequestPayload.setStatus(optionsMaster.getStatus());
					optionsMastersDTOs.add(optionMasterRequestPayload);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", optionsMastersDTOs);
				responseMap.put("count", optionsMasters.getTotalElements());
				
				stopWatch.stop();
				logger.info("Get Option Master Service Success");
				logger.info(String.format("Time taken on Get Option Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Option Master Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("Get Option Master Service failed");
				logger.info(String.format("Time taken on Add Option Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Option Master Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Option Master Service.", e);
			logger.info(
					String.format("Time taken on Get Option Master Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Option Master Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateOptionMaster(OptionMasterRequestPayload payload, Long userID) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Update Option Master Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			OptionsMaster optionMasterExist = optionsMasterRepository.findById(Long.parseLong(payload.getId()))
					.orElse(null);
			if (optionMasterExist != null) {
				optionMasterExist.setUpdatedBy(userID.toString());
				optionMasterExist.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				optionMasterExist.setOptionDesc(payload.getOptionDesc());
				optionMasterExist.setOptionName(payload.getOptionName());
				optionMasterExist.setOptionValue(payload.getOptionValue());
				optionMasterExist.setStatus(payload.getStatus());
				optionsMasterRepository.save(optionMasterExist);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Option Master Service Success");
				logger.info(String.format("Time taken on Update Option Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Option Master Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Option Master Service failed");
				logger.info("Option Master Not Exist :- ");
				logger.info(String.format("Time taken on Update Option Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Option Master Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Option Master Service.", e);
			logger.info(
					String.format("Time taken on Update Option Master Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Option Master Service.");
			return responseMap;
		}
	}
}
