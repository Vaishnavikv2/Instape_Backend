package com.instape.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
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

import com.instape.app.cloudsql.model.PartnerServiceEndPointsDTO;
import com.instape.app.cloudsql.model.PartnerServiceEndpoints;
import com.instape.app.cloudsql.model.PartnerServiceValues;
import com.instape.app.cloudsql.model.PartnerServiceValuesDTO;
import com.instape.app.cloudsql.model.PartnerServices;
import com.instape.app.cloudsql.repository.PartnerServiceEndpointsRepository;
import com.instape.app.cloudsql.repository.PartnerServiceRepository;
import com.instape.app.cloudsql.repository.PartnerServiceValuesRepository;
import com.instape.app.request.PartnerServiceDetailsRequestPayload;
import com.instape.app.request.PartnerServiceEndPointsRequestPayload;
import com.instape.app.request.PartnerServiceValuesRequestPayload;
import com.instape.app.service.PartnerServicesEndpointsService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 29-Oct-2024
 * @ModifyDate - 29-Oct-2024
 * @Desc -
 */
@Service
public class PartnerServicesEndpointsServiceImpl implements PartnerServicesEndpointsService {

	private final Logger logger = LogManager.getLogger(PartnerServicesEndpointsServiceImpl.class);

	@Autowired
	private PartnerServiceRepository partnerServiceRepository;;

	@Autowired
	private PartnerServiceEndpointsRepository partnerServiceEndPointsRepository;

	@Autowired
	private PartnerServiceValuesRepository partnerServiceValuesRepository;

	@Override
	public Map<Object, Object> addPartnerServiceEndPoints(PartnerServiceEndPointsRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Create Partner Services EndPoints Service");
			PartnerServices partnerService = partnerServiceRepository
					.findById(Long.parseLong(payload.getPartnerServiceId())).orElse(null);
			if (partnerService != null) {
				PartnerServiceEndPointsDTO dataPayload = payload.getData();
				PartnerServiceEndpoints keyExist = partnerServiceEndPointsRepository
						.findByLenderServiceIdAndKey(payload.getPartnerServiceId(), dataPayload.getKey());

				if (keyExist != null) {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.KEYEXIST);
					responseMap.put("status", CommonMessageLog.FAILED);
					stopWatch.stop();
					logger.info("Create Partner Services EndPoints failed");
					logger.info(String.format("Time taken on Create Partner Services EndPoints Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Create Partner Services EndPoints Service %s",
							CommonMessageLog.KEYEXIST));
					return responseMap;
				}

				PartnerServiceEndpoints partnerServiceEndpoints = new PartnerServiceEndpoints();
				partnerServiceEndpoints.setPartnerServices(partnerService);
				partnerServiceEndpoints.setEndpoint(dataPayload.getPartnerServiceEndPoint());
				partnerServiceEndpoints.setKey(dataPayload.getKey());
				partnerServiceEndpoints.setStatus(PortalConstant.ACTIVE);
				partnerServiceEndpoints.setDeleted(false);
				partnerServiceEndpoints.setCreatedBy(Long.parseLong(payload.getUserId()));
				partnerServiceEndpoints.setCreatedDate(DateUtils.getCurrentTimestamp());
				partnerServiceEndpoints.setUpdatedBy(Long.parseLong(payload.getUserId()));
				partnerServiceEndpoints.setUpdatedDate(DateUtils.getCurrentTimestamp());
				partnerServiceEndPointsRepository.save(partnerServiceEndpoints);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Create Partner Services EndPoints Success");
				logger.info(String.format("Time taken on Create Partner Services EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Create Partner Services EndPoints Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.PARTNERSERVICENOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Create Partner EndPoints failed");
				logger.info(String.format("Time taken on Create Partner Services EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Create Partner Services EndPoints Service %s",
						CommonMessageLog.PARTNERSERVICENOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Create Partner Services EndPoints Service.", e);
			logger.info(String.format("Time taken on Create Partner Services EndPoints Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Partner Services EndPoints Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getPartnerServiceEndPoints(PartnerServiceDetailsRequestPayload payload,
			PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Get Partner Services EndPoints Service");
			Page<PartnerServiceEndpoints> partnerServiceEndpoints = partnerServiceEndPointsRepository
					.findByLenderServiceId(payload.getPartnerServiceId(), pageRequest);
			if (partnerServiceEndpoints != null && !partnerServiceEndpoints.isEmpty()) {
				List<PartnerServiceEndPointsDTO> lenderServiceEndpointDTOs = new ArrayList<PartnerServiceEndPointsDTO>();
				for (PartnerServiceEndpoints partnerServiceEndpoint : partnerServiceEndpoints) {
					PartnerServiceEndPointsDTO partnerServiceEndPointsDTO = new PartnerServiceEndPointsDTO();
					partnerServiceEndPointsDTO.setDeleted(partnerServiceEndpoint.isDeleted() ? "true" : "false");
					partnerServiceEndPointsDTO.setId(partnerServiceEndpoint.getId().toString());
					partnerServiceEndPointsDTO.setKey(partnerServiceEndpoint.getKey());
					partnerServiceEndPointsDTO.setPartnerServiceEndPoint(partnerServiceEndpoint.getEndpoint());
					partnerServiceEndPointsDTO.setStatus(partnerServiceEndpoint.getStatus());
					lenderServiceEndpointDTOs.add(partnerServiceEndPointsDTO);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", lenderServiceEndpointDTOs);
				responseMap.put("count", partnerServiceEndpoints.getTotalElements());
				stopWatch.stop();
				logger.info("Get Partner Services EndPoints Success");
				logger.info(String.format("Time taken on Get Partner Services EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner Services EndPoints Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("Get Partner Services EndPoints failed");
				logger.info(String.format("Time taken on Get Partner Services EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner Services EndPoints Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Partner Services EndPoints Service.", e);
			logger.info(String.format("Time taken on Get Partner Services EndPoints Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Services EndPoints Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updatePartnerServiceEndPoints(PartnerServiceEndPointsDTO payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Partner Services EndPoints Service");
			PartnerServiceEndpoints partnerServiceEndpointExist = partnerServiceEndPointsRepository
					.findById(Long.parseLong(payload.getId())).orElse(null);
			if (partnerServiceEndpointExist != null) {
				partnerServiceEndpointExist.setEndpoint(payload.getPartnerServiceEndPoint());
				partnerServiceEndpointExist.setStatus(payload.getStatus());
				partnerServiceEndpointExist.setDeleted(Boolean.parseBoolean(payload.getDeleted()));
				partnerServiceEndpointExist.setUpdatedBy(Long.parseLong(payload.getUserId()));
				partnerServiceEndpointExist.setUpdatedDate(DateUtils.getCurrentTimestamp());
				partnerServiceEndPointsRepository.save(partnerServiceEndpointExist);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Partner Services EndPoints Success");
				logger.info(String.format("Time taken on Update Partner Services EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Update Partner Services EndPoints Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Partner Services EndPoints failed");
				logger.info(String.format("Time taken on Update Partner Services EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Partner Services EndPoints Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Partner Services EndPoints Service.", e);
			logger.info(String.format("Time taken on Update Partner Services EndPoints Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Services EndPoints Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> addPartnerServiceValues(PartnerServiceValuesRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Create Partner Services Values Service");
			PartnerServices partnerService = partnerServiceRepository
					.findById(Long.parseLong(payload.getPartnerServiceId())).orElse(null);
			if (partnerService != null) {
				PartnerServiceValuesDTO dataPayload = payload.getData();
				PartnerServiceValues keyExist = partnerServiceValuesRepository
						.findByPartnerServiceIdAndKey(payload.getPartnerServiceId(), dataPayload.getKey());

				if (keyExist != null) {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.KEYEXIST);
					responseMap.put("status", CommonMessageLog.FAILED);
					stopWatch.stop();
					logger.info("Create Partner Services Values failed");
					logger.info(String.format("Time taken on Create Partner Services Values Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Create Partner Services Values Service %s",
							CommonMessageLog.KEYEXIST));
					return responseMap;
				}

				PartnerServiceValues lenderServiceValues = new PartnerServiceValues();
				lenderServiceValues.setPartnerServices(partnerService);
				lenderServiceValues.setValue(dataPayload.getValue());
				lenderServiceValues.setKey(dataPayload.getKey());
				lenderServiceValues.setStatus(PortalConstant.ACTIVE);
				lenderServiceValues.setDeleted(false);
				lenderServiceValues.setCreatedBy(Long.parseLong(payload.getUserId()));
				lenderServiceValues.setCreatedDate(DateUtils.getCurrentTimestamp());
				lenderServiceValues.setUpdatedBy(Long.parseLong(payload.getUserId()));
				lenderServiceValues.setUpdatedDate(DateUtils.getCurrentTimestamp());
				partnerServiceValuesRepository.save(lenderServiceValues);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Create Partner Services Values Success");
				logger.info(String.format("Time taken on Create Partner Services Values Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Create Partner Services Values Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.PARTNERSERVICENOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Create Partner Values failed");
				logger.info(String.format("Time taken on Create Partner Services Values Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Create Partner Services Values Service %s",
						CommonMessageLog.PARTNERSERVICENOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Create Partner Services Values Service.", e);
			logger.info(String.format("Time taken on Create Partner Services Values Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Partner Services Values Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getPartnerServiceValues(PartnerServiceDetailsRequestPayload payload,
			PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Get Partner Services Values Service");
			Page<PartnerServiceValues> partnerServiceValues = partnerServiceValuesRepository
					.findByPartnerServiceId(payload.getPartnerServiceId(), pageRequest);
			if (partnerServiceValues != null && !partnerServiceValues.isEmpty()) {
				List<PartnerServiceValuesDTO> lenderServiceValuesDTOs = new ArrayList<PartnerServiceValuesDTO>();
				for (PartnerServiceValues partnerServiceValue : partnerServiceValues) {
					PartnerServiceValuesDTO partnerServiceValuesDTO = new PartnerServiceValuesDTO();
					partnerServiceValuesDTO.setDeleted(partnerServiceValue.isDeleted() ? "true" : "false");
					partnerServiceValuesDTO.setId(partnerServiceValue.getId().toString());
					partnerServiceValuesDTO.setKey(partnerServiceValue.getKey());
					partnerServiceValuesDTO.setValue(partnerServiceValue.getValue());
					partnerServiceValuesDTO.setStatus(partnerServiceValue.getStatus());
					lenderServiceValuesDTOs.add(partnerServiceValuesDTO);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", lenderServiceValuesDTOs);
				responseMap.put("count", partnerServiceValues.getTotalElements());
				stopWatch.stop();
				logger.info("Get Partner Services Values Success");
				logger.info(String.format("Time taken on Get Partner Services Values Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner Services Values Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("Get Partner Services Values failed");
				logger.info(String.format("Time taken on Get Partner Services Values Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Lender Services Values Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Partner Services Values Service.", e);
			logger.info(String.format("Time taken on Get Partner Services Values Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Services Values Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updatePartnerServiceValues(PartnerServiceValuesDTO payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Partner Services Values Service");
			PartnerServiceValues partnerServiceValueExist = partnerServiceValuesRepository
					.findById(Long.parseLong(payload.getId())).orElse(null);
			if (partnerServiceValueExist != null) {
				partnerServiceValueExist.setValue(payload.getValue());
				partnerServiceValueExist.setStatus(payload.getStatus());
				partnerServiceValueExist.setDeleted(Boolean.parseBoolean(payload.getDeleted()));
				partnerServiceValueExist.setUpdatedBy(Long.parseLong(payload.getUserId()));
				partnerServiceValueExist.setUpdatedDate(DateUtils.getCurrentTimestamp());
				partnerServiceValuesRepository.save(partnerServiceValueExist);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Partner Services Values Success");
				logger.info(String.format("Time taken on Update Partner Services Values Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Partner Services Values Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Partner Services Values failed");
				logger.info(String.format("Time taken on Update Partner Services Values Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Partner Services Values Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Partner Services Values Service.", e);
			logger.info(String.format("Time taken on Update Partner Services Values Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Services Values Service.");
			return responseMap;
		}
	}
}
