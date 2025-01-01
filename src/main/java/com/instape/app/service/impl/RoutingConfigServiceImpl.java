package com.instape.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.instape.app.cloudsql.model.Partner;
import com.instape.app.cloudsql.model.PartnerServices;
import com.instape.app.cloudsql.model.ProductCatalog;
import com.instape.app.cloudsql.model.RoutingConfig;
import com.instape.app.cloudsql.model.RoutingConfigDTO;
import com.instape.app.cloudsql.repository.PartnerRepository;
import com.instape.app.cloudsql.repository.PartnerServiceRepository;
import com.instape.app.cloudsql.repository.ProductCatalogRepository;
import com.instape.app.cloudsql.repository.RoutingConfigRepository;
import com.instape.app.response.RoutingConfigResponsePayload;
import com.instape.app.service.RoutingConfigService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Nov-2024
 * @ModifyDate - 04-Nov-2024
 * @Desc -
 */
@Service
public class RoutingConfigServiceImpl implements RoutingConfigService {

	static final Logger logger = LogManager.getFormatterLogger(RoutingConfigServiceImpl.class);

	@Autowired
	private ProductCatalogRepository productCatalogRepository;

	@Autowired
	private PartnerRepository partnerRepository;

	@Autowired
	private PartnerServiceRepository partnerServiceRepository;

	@Autowired
	private RoutingConfigRepository routingConfigRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Map<Object, Object> addRoutingConfig(RoutingConfigDTO payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
			logger.info("Start of Add Routing Config Service ");
			RoutingConfig routingConfigExist = routingConfigRepository.checkForDuplicateRoutingConfig(
					Long.parseLong(payload.getProductId()), Long.parseLong(payload.getPartnerId()),
					Long.parseLong(payload.getPartnerServiceId()), payload.getScreen());
			if (routingConfigExist != null) {
				stopWatch.stop();
				logger.info("Add Routing Config Failed");
				logger.info(String.format("Time taken on Add Routing Config Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Routing Config Service %s", CommonMessageLog.FAILED));
				return returnResponseMap(PortalConstant.BADREQUEST, CommonMessageLog.FAILED,
						CommonMessageLog.ROUTINGCONFIGALREADYEXIST);
			}

			ProductCatalog productExist = productCatalogRepository.findById(Long.parseLong(payload.getProductId()))
					.orElse(null);
			Partner partnerExist = partnerRepository.findById(Long.parseLong(payload.getPartnerId())).orElse(null);
			PartnerServices partnerServiceExist = partnerServiceRepository
					.findById(Long.parseLong(payload.getPartnerServiceId())).orElse(null);

			if (productExist == null || partnerExist == null || partnerServiceExist == null) {
				stopWatch.stop();
				logger.info("Add Routing Config Failed");
				logger.info(String.format("Time taken on Add Routing Config Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Routing Config Service %s", CommonMessageLog.FAILED));
				return returnResponseMap(PortalConstant.BADREQUEST, CommonMessageLog.FAILED,
						CommonMessageLog.PRODUCTPARTNERANDSERVICEIDNOTFOUND);
			}

			RoutingConfig routingConfig = new RoutingConfig();
			routingConfig.setProductCatalog(productExist);
			routingConfig.setPartner(partnerExist);
			routingConfig.setPartnerServices(partnerServiceExist);
			routingConfig.setCommand(payload.getCommand());
			routingConfig.setScreen(payload.getScreen());
			routingConfig.setCategory(payload.getCategory());
			routingConfig.setTopicId(payload.getTopicId());
			routingConfig.setKeyword(payload.getKeyword());
			routingConfig.setAuthRequired(payload.getAuthRequired());
			routingConfig.setRestEndpoint(payload.getRestEndpoint());
			routingConfig.setStatus(PortalConstant.ACTIVE);
			routingConfig.setDeleted(false);
			routingConfig.setCreatedBy(Long.parseLong(payload.getUserId()));
			routingConfig.setUpdatedBy(Long.parseLong(payload.getUserId()));
			routingConfig.setCreatedDate(DateUtils.getCurrentTimestamp());
			routingConfig.setUpdatedDate(DateUtils.getCurrentTimestamp());
			routingConfigRepository.save(routingConfig);

			stopWatch.stop();
			logger.info("Add Routing Config Success");
			logger.info(
					String.format("Time taken on Add Routing Config Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Add Routing Config Service %s", CommonMessageLog.SUCCESS));
			return returnResponseMap(PortalConstant.OK, CommonMessageLog.SUCCESS, CommonMessageLog.SUCCESS);
		} catch (Exception e) {
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add Routing Config Service.", e);
			logger.info(
					String.format("Time taken on Add Routing Config Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Routing Config Service.");
			return returnResponseMap(PortalConstant.INTERNALSERVERERROR, CommonMessageLog.FAILED, e.getMessage());
		}
	}

	public Map<Object, Object> returnResponseMap(int code, String status, String message) {
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		responseMap.put("code", code);
		responseMap.put("status", status);
		responseMap.put("message", message);
		return responseMap;
	}

	@Override
	public Map<Object, Object> getRotingConfigs(PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Get Routing Config Service ");

			Page<RoutingConfig> routingConfigs = routingConfigRepository.getAllRoutingConfigs(pageRequest);
			if (routingConfigs == null || routingConfigs.isEmpty()) {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("Get Routing Config Failed");
				logger.info(String.format("Time taken on Get Routing Config Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Routing Config Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}

			List<RoutingConfig> routingConfigsList = routingConfigs.getContent();
			List<RoutingConfigResponsePayload> routingConfigResponse = new ArrayList<RoutingConfigResponsePayload>();

			for (RoutingConfig routingConfig : routingConfigsList) {
				RoutingConfigResponsePayload configResponse = new RoutingConfigResponsePayload();
				configResponse.setId(String.valueOf(routingConfig.getId()));
				configResponse.setCategory(routingConfig.getCategory());
				configResponse.setTopicId(routingConfig.getTopicId());
				configResponse.setKeyword(routingConfig.getKeyword());
				configResponse.setAuthRequired(routingConfig.getAuthRequired());
				configResponse.setRestEndpoint(routingConfig.getRestEndpoint());
				configResponse.setStatus(routingConfig.getStatus());
				configResponse.setDeleted(routingConfig.isDeleted());
				configResponse.setScreen(routingConfig.getScreen());
				configResponse.setCommand(routingConfig.getCommand());
				configResponse.setProductId(routingConfig.getProductCatalog().getId());
				configResponse.setPartnerId(routingConfig.getPartner().getId());
				configResponse.setPartnerServiceId(routingConfig.getPartnerServices().getId());
				configResponse.setProductCode(routingConfig.getProductCatalog().getCode());
				configResponse.setPartnerCode(routingConfig.getPartner().getCode());
				configResponse.setPartnerServiceCode(routingConfig.getPartnerServices().getCode());
				routingConfigResponse.add(configResponse);
			}

			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			responseMap.put("data", routingConfigResponse);

			stopWatch.stop();
			logger.info("Get Routing Config Success");
			logger.info(
					String.format("Time taken on Get Routing Config Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Get Routing Config Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Routing Config Service.", e);
			logger.info(
					String.format("Time taken on Get Routing Config Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Routing Config Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateRoutingConfig(RoutingConfigDTO payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
			logger.info("Start of Update Routing Config Service ");
			RoutingConfig routingConfigExist = routingConfigRepository.findById(Long.parseLong(payload.getId()))
					.orElse(null);
			if (routingConfigExist == null) {
				stopWatch.stop();
				logger.info("Update Routing Config Failed");
				logger.info(String.format("Time taken on Update Routing Config Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Routing Config Service %s", CommonMessageLog.FAILED));
				return returnResponseMap(PortalConstant.BADREQUEST, CommonMessageLog.FAILED,
						CommonMessageLog.RECORDSNOTFOUND);
			}

			RoutingConfig routingConfig = routingConfigExist;
			routingConfig.setCommand(payload.getCommand());
			routingConfig.setCategory(payload.getCategory());
			routingConfig.setTopicId(payload.getTopicId());
			routingConfig.setKeyword(payload.getKeyword());
			routingConfig.setAuthRequired(payload.getAuthRequired());
			routingConfig.setRestEndpoint(payload.getRestEndpoint());
			routingConfig.setStatus(payload.getStatus());
			routingConfig.setDeleted(payload.isDeleted());
			routingConfig.setUpdatedBy(Long.parseLong(payload.getUserId()));
			routingConfig.setUpdatedDate(DateUtils.getCurrentTimestamp());
			routingConfigRepository.save(routingConfig);

			stopWatch.stop();
			logger.info("Update Routing Config Success");
			logger.info(String.format("Time taken on Update Routing Config Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Update Routing Config Service %s", CommonMessageLog.SUCCESS));
			return returnResponseMap(PortalConstant.OK, CommonMessageLog.SUCCESS, CommonMessageLog.SUCCESS);
		} catch (Exception e) {
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Routing Config Service.", e);
			logger.info(String.format("Time taken on Update Routing Config Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Routing Config Service.");
			return returnResponseMap(PortalConstant.INTERNALSERVERERROR, CommonMessageLog.FAILED, e.getMessage());
		}
	}
}
