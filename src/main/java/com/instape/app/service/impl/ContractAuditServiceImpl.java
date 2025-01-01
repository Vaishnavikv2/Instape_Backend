package com.instape.app.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.instape.app.cloudsql.model.ContractAuditLogs;
import com.instape.app.cloudsql.model.ContractAuditLogsDTO;
import com.instape.app.cloudsql.model.ContractAuditTimestampDTO;
import com.instape.app.cloudsql.model.Users;
import com.instape.app.cloudsql.repository.ContractAuditLogsRepository;
import com.instape.app.cloudsql.repository.UserRepository;
import com.instape.app.request.ContractDetailRequestPayload;
import com.instape.app.request.DownloadFilesRequestPayload;
import com.instape.app.service.ContractAuditService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;

@Service
public class ContractAuditServiceImpl implements ContractAuditService {

	static final Logger logger = LogManager.getFormatterLogger(ContractAuditServiceImpl.class);

	@Autowired
	private ContractAuditLogsRepository contractAuditLogsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Map<Object, Object> getContractAuditTimestamps(ContractDetailRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of get Contract Audit Timestamps Service");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<ContractAuditLogs> contractAuditLogs = contractAuditLogsRepository
					.getAllContractAuditTimestampsByContractCode(payload.getContractCode());
			if (contractAuditLogs != null && !contractAuditLogs.isEmpty()) {
				List<ContractAuditTimestampDTO> contractAuditTimestampDTOs = new ArrayList<ContractAuditTimestampDTO>();
				for (ContractAuditLogs contractAuditLog : contractAuditLogs) {
					ContractAuditTimestampDTO contractAuditTimestampDTO = new ContractAuditTimestampDTO();
					contractAuditTimestampDTO.setContractId(contractAuditLog.getId());
					contractAuditTimestampDTO.setTimestamp(contractAuditLog.getUpdatedDate());
					contractAuditTimestampDTOs.add(contractAuditTimestampDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", contractAuditTimestampDTOs);
				stopWatch.stop();
				logger.info("Get Contract Audit Timestamps Success");
				logger.info(String.format("Time taken on get Contract Audit Timestamps Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Contract Audit Timestamps Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Contract Audit Timestamps failed");
				logger.info(String.format("Contracts Audits Records Not Found :- "));
				logger.info(String.format("Time taken on get Contract Audit Timestamps Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Contract Audit Timestamps Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Contract Audit Timestamps Service.", e);
			logger.info(String.format("Time taken on get Contract Audit Timestamps Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Contract Audit Timestamps Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getContractAuditLogs(DownloadFilesRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of get Contract Audit Logs Service");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<ContractAuditLogsDTO> auditLogsDTOs = new ArrayList<ContractAuditLogsDTO>();
			for (Long contractAuditLogsId : payload.getData()) {
				ContractAuditLogs log = contractAuditLogsRepository.findById(contractAuditLogsId).orElse(null);
				ContractAuditLogsDTO auditLogsDTO = modelMapper.map(log, ContractAuditLogsDTO.class);
				if (log.getUpdatedBy() != null && !log.getUpdatedBy().isEmpty()) {
					Users user = userRepository.findById(Long.parseLong(log.getUpdatedBy())).orElse(null);
					if (user != null) {
						auditLogsDTO.setUserName(user.getFullName());
					}
				}
				auditLogsDTOs.add(auditLogsDTO);
			}
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			responseMap.put("firstRecord", auditLogsDTOs.get(0));
			responseMap.put("secondRecord", auditLogsDTOs.get(1));
			stopWatch.stop();
			logger.info("Get Contract Audit Logs Success");
			logger.info(String.format("Time taken on get Contract Audit Logs Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of get Contract Audit Logs Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Contract Audit Logs Service.", e);
			logger.info(String.format("Time taken on get Contract Audit Logs Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Contract Audit Logs Service.");
			return responseMap;
		}
	}
}
