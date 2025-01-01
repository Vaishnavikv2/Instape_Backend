package com.instape.app.service.impl;

import java.util.ArrayList;
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
import com.instape.app.cloudsql.model.CustomerMaster;
import com.instape.app.cloudsql.model.UserInfo;
import com.instape.app.cloudsql.model.UserInfoDTO;
import com.instape.app.cloudsql.repository.UserInfoRepository;
import com.instape.app.request.UserDetailsRequestPayload;
import com.instape.app.service.PortalUserService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Jun-2024
 * @ModifyDate - 28-Jun-2024
 * @Desc -
 */
@Service
public class PortalUserServiceImpl implements PortalUserService {

	static final Logger logger = LogManager.getFormatterLogger(PortalUserServiceImpl.class);

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Override
	public Map<Object, Object> getUsers(UserDetailsRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of get User Details Service");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<UserInfo> userDetailsPage = null;
			if ((payload.getUserName() != null && !payload.getUserName().isEmpty())
					|| (payload.getCustomerName() != null && !payload.getCustomerName().isEmpty())) {
				if (payload.getUserName() != null && !payload.getUserName().isEmpty()) {
					payload.setUserName("%" + payload.getUserName() + "%");
				}
				if (payload.getCustomerName() != null && !payload.getCustomerName().isEmpty()) {
					payload.setCustomerName("%" + payload.getCustomerName() + "%");
				}
				userDetailsPage = userInfoRepository.getUserDetailsUsingFilters(payload.getUserName(),
						payload.getCustomerName(), pageRequest, PortalConstant.ACTIVE);
			} else {
				userDetailsPage = userInfoRepository.getAllUserDetailsByStatus(PortalConstant.ACTIVE, pageRequest);
			}
			List<UserInfo> userDetailsList = userDetailsPage.getContent();
			if (userDetailsList != null && !userDetailsList.isEmpty()) {
				List<UserInfoDTO> userInfoDTOs = new ArrayList<UserInfoDTO>();
				for (UserInfo userInfo : userDetailsList) {
					UserInfoDTO userInfoDTO = new UserInfoDTO();
					userInfoDTO.setUserId(userInfo.getId().toString());
					userInfoDTO.setUserName(userInfo.getUserName());
					userInfoDTO.setEmail(userInfo.getEmail());
					userInfoDTO.setDesignation(userInfo.getDesignation());
					userInfoDTO.setGender(userInfo.getGender());
					userInfoDTO.setRole(userInfo.getRole());
					userInfoDTO.setStatus(userInfo.getStatus());
					if (userInfo.getCustomerMaster() != null) {
						CustomerMaster customerMaster = userInfo.getCustomerMaster();
						userInfoDTO.setCustomerName(customerMaster.getCustomerName());
						userInfoDTO.setCustomerCode(customerMaster.getCode());
					}
					userInfoDTOs.add(userInfoDTO);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", userDetailsPage.getTotalElements());
				responseMap.put("data", userInfoDTOs);
				stopWatch.stop();
				logger.info("Get User Details Success");
				logger.info(
						String.format("Time taken on get User Details Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get User Details Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get User Details failed");
				logger.info(String.format("User Records Not Found :- "));
				logger.info(
						String.format("Time taken on get User Details Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get User Details Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get User Details Service.", e);
			logger.info(String.format("Time taken on get User Details Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get User Details Service.");
			return responseMap;
		}
	}
}
