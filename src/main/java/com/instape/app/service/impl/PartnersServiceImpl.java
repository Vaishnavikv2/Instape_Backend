package com.instape.app.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;
import com.instape.app.cloudsql.model.Contract;
import com.instape.app.cloudsql.model.ContractDTO;
import com.instape.app.cloudsql.model.DownloadExecutionFileInfoDataDTO;
import com.instape.app.cloudsql.model.DownloadFileInfoDataDTO;
import com.instape.app.cloudsql.model.Partner;
import com.instape.app.cloudsql.model.PartnerAPIsDTO;
import com.instape.app.cloudsql.model.PartnerAPIsTestCaseDTO;
import com.instape.app.cloudsql.model.PartnerApiTestCaseExecution;
import com.instape.app.cloudsql.model.PartnerApis;
import com.instape.app.cloudsql.model.PartnerApisTestCases;
import com.instape.app.cloudsql.model.PartnerClientContract;
import com.instape.app.cloudsql.model.PartnerConstants;
import com.instape.app.cloudsql.model.PartnerConstantsDTO;
import com.instape.app.cloudsql.model.PartnerContacts;
import com.instape.app.cloudsql.model.PartnerContactsDTO;
import com.instape.app.cloudsql.model.PartnerContracts;
import com.instape.app.cloudsql.model.PartnerContractsDTO;
import com.instape.app.cloudsql.model.PartnerFileInfoDTO;
import com.instape.app.cloudsql.model.PartnerFilesUpload;
import com.instape.app.cloudsql.model.PartnerNotes;
import com.instape.app.cloudsql.model.PartnerNotesDTO;
import com.instape.app.cloudsql.repository.ContractRepository;
import com.instape.app.cloudsql.repository.PartnerAPIsRepository;
import com.instape.app.cloudsql.repository.PartnerAPIsTestCaseRepository;
import com.instape.app.cloudsql.repository.PartnerApiTestCaseExecutionRepository;
import com.instape.app.cloudsql.repository.PartnerClientContractRepository;
import com.instape.app.cloudsql.repository.PartnerConstantsRepository;
import com.instape.app.cloudsql.repository.PartnerContactsRepository;
import com.instape.app.cloudsql.repository.PartnerContractsRepository;
import com.instape.app.cloudsql.repository.PartnerNotesRepository;
import com.instape.app.cloudsql.repository.PartnerRepository;
import com.instape.app.cloudsql.repository.PartnersFileUploadRepository;
import com.instape.app.cloudstore.service.CloudPubSubService;
import com.instape.app.cloudstore.service.CloudStorageService;
import com.instape.app.request.APITestCaseRequestPayload;
import com.instape.app.request.AssignContractRequestPayload;
import com.instape.app.request.ExecuteTestCaseRequestPayload;
import com.instape.app.request.PartnerAPIRequestPayload;
import com.instape.app.request.PartnerAPIsRequestPayload;
import com.instape.app.request.PartnerConstantsRequestPayload;
import com.instape.app.request.PartnerContactsRequestPayload;
import com.instape.app.request.PartnerContractRequestPayload;
import com.instape.app.request.PartnerNotesRequestPayload;
import com.instape.app.request.PartnersRequestPayload;
import com.instape.app.request.RegisterPartnerRequestPayload;
import com.instape.app.request.RotateTokenRequestPayload;
import com.instape.app.request.TestCaseExecutionPayload;
import com.instape.app.request.TestCaseExecutionsRequestPayload;
import com.instape.app.request.UpdateTestCaseExecutionRequestPayload;
import com.instape.app.response.AccessKeyResponsePayload;
import com.instape.app.response.ExecutionStatusResponsePayload;
import com.instape.app.response.LocalFileUploadResponse;
import com.instape.app.service.PartnersService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.FirebaseScreen;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 29-Jul-2024
 * @ModifyDate - 29-Jul-2024
 * @Desc -
 */
@Service
public class PartnersServiceImpl implements PartnersService {

	static final Logger logger = LogManager.getFormatterLogger(PartnersServiceImpl.class);

	@Autowired
	private PartnerRepository partnersRepository;

	@Value("${BUCKET_NAME}")
	private String BUCKET_NAME;

	@Value("${PROJECT_ID}")
	private String PROJECT_ID;

	@Value("${RUNNER-X-API-KEY}")
	private String runnerXApiKey;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private PartnerContractsRepository partnerContractsRepository;

	@Autowired
	private CloudStorageService cloudStorageService;

	@Autowired
	private PartnersFileUploadRepository partnersFileUploadRepository;

	@Autowired
	private PartnerConstantsRepository partnerConstantsRepository;

	@Autowired
	private PartnerContactsRepository partnerContactsRepository;

	@Autowired
	private PartnerNotesRepository partnerNotesRepository;

	@Autowired
	private PartnerAPIsRepository partnerAPIsRepository;

	@Autowired
	private PartnerAPIsTestCaseRepository partnerAPIsTestCaseRepository;

	@Autowired
	private CloudPubSubService cloudPubSubService;

	private String publishTestCaseExecuteTopicId = "partner-api-execution-queue";

	@Autowired
	private PartnerApiTestCaseExecutionRepository partnerApiTestCaseExecutionRepository;

	@Autowired
	private PartnerClientContractRepository partnerClientContractRepository;

	@Override
	public Map<Object, Object> registerPartner(RegisterPartnerRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Register Partner Service");
			Partner partner = new Partner();
			partner.setPartnerName(payload.getPartnerName());
			partner.setType(payload.getPartnerType());
			partner.setStartDate(Timestamp.valueOf(payload.getStartDate()));
			partner.setEndDate(Timestamp.valueOf(payload.getEndDate()));
			partner.setStatus(payload.getStatus());
			partner.setDeleted(false);
			partner.setCinNumber(payload.getCinNumber());
			partner.setGstNumber(payload.getGstNumber());
			partner.setLogoPath(payload.getLogoPath());
			partner.setParterAddress(payload.getPartnerAddress());
			partner.setWebsite(payload.getWebsite());
			partner.setPartnerPhone(payload.getPartnerPhone());
			partner.setPartnerEmail(payload.getPartnerEmail());
			partner.setCreatedBy(Long.parseLong(payload.getUserId()));
			partner.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
			partner.setUpdatedBy(Long.parseLong(payload.getUserId()));
			partner.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
			UUID uuid = UUID.randomUUID();
			String shortUuid = uuid.toString().substring(0, 16);
			partner.setCode(payload.getPartnerCode() + "-" + shortUuid);
			partnersRepository.save(partner);

			logger.info("Partner Registered in DB Success");
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info("Register Partner Success");
			logger.info(String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Register Partner Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Register Partner Service.", e);
			logger.info(String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Register Partner Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getPartners(PartnersRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info("Start of get Registered Partners Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<Partner> partners = null;

			if (payload.getPartnerName() != null && !payload.getPartnerName().isEmpty()) {
				partners = partnersRepository.findByPartnerName("%" + payload.getPartnerName() + "%", pageRequest);
			} else if (payload.getPartnerType() != null && !payload.getPartnerType().isEmpty()) {
				partners = partnersRepository.findByPartnerType("%" + payload.getPartnerType() + "%", pageRequest);
			} else {
				partners = partnersRepository.findAllPartners(pageRequest);
			}

			if (partners != null && !partners.isEmpty()) {
				logger.info("Registered Partners Recods Exist");
				List<RegisterPartnerRequestPayload> responsePayloads = new ArrayList<RegisterPartnerRequestPayload>();
				for (Partner partner : partners) {
					RegisterPartnerRequestPayload response = new RegisterPartnerRequestPayload();
					response.setPartnerName(partner.getPartnerName());
					response.setPartnerType(partner.getType());
					response.setPartnerCode(partner.getCode());
					response.setStartDate(partner.getStartDate().toString());
					response.setEndDate(partner.getEndDate().toString());
					response.setStatus(partner.getStatus());
					response.setDeleted(partner.isDeleted() ? "true" : "false");
					response.setPartnerId(partner.getId().toString());
					response.setCinNumber(partner.getCinNumber());
					response.setGstNumber(partner.getGstNumber());
					response.setLogoPath(partner.getLogoPath());
					response.setPartnerAddress(partner.getParterAddress());
					response.setWebsite(partner.getWebsite());
					response.setPartnerPhone(partner.getPartnerPhone());
					response.setPartnerEmail(partner.getPartnerEmail());
					responsePayloads.add(response);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", partners.getTotalElements());
				responseMap.put("data", responsePayloads);
				stopWatch.stop();
				logger.info("Get Registered Partners Service Success");
				logger.info(String.format("Time taken on get Registered Partners Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Registered Partners Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("Get Registered Partners Service failed");
				logger.info("Registered Partners Not Found :- ");
				logger.info(String.format("Time taken on get Registered Partners Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of get Registered Partners Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Registered Partners Service.", e);
			logger.info(String.format("Time taken on get Registered Partners Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Registered Partners Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updatePartner(RegisterPartnerRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Register Partner Service");
			Partner partnerExist = partnersRepository.findByCode(payload.getPartnerCode());
			if (partnerExist != null) {
				partnerExist.setPartnerName(payload.getPartnerName());
				partnerExist.setType(payload.getPartnerType());
				partnerExist.setStartDate(Timestamp.valueOf(payload.getStartDate()));
				partnerExist.setEndDate(Timestamp.valueOf(payload.getEndDate()));
				partnerExist.setStatus(payload.getStatus());
				partnerExist.setDeleted(Boolean.valueOf(payload.getDeleted()));
				partnerExist.setCinNumber(payload.getCinNumber());
				partnerExist.setGstNumber(payload.getGstNumber());
				partnerExist.setLogoPath(payload.getLogoPath());
				partnerExist.setParterAddress(payload.getPartnerAddress());
				partnerExist.setWebsite(payload.getWebsite());
				partnerExist.setPartnerPhone(payload.getPartnerPhone());
				partnerExist.setPartnerEmail(payload.getPartnerEmail());
				partnerExist.setUpdatedBy(Long.parseLong(payload.getUserId()));
				partnerExist.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				partnersRepository.save(partnerExist);

				logger.info("Register of Partner in DB Success");
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Register Partner Success");
				logger.info(
						String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Register Partner Service %s", CommonMessageLog.SUCCESS));
				return responseMap;

			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Register Partner Service failed");
				logger.info("Registered Partner Not Found :- ");
				logger.info(String.format("Time taken on Update Register Partner Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Update Register Partner Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Register Partner Service.", e);
			logger.info(String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Register Partner Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> rotateToken(RotateTokenRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Register Partner Service");

			// find partner object by code

			// set rotate key

			// save it into db

			logger.info("Register of Partner in DB Success");
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info("Register Partner Success");
			logger.info(String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Register Partner Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Register Partner Service.", e);
			logger.info(String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Register Partner Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> generateAccessKey(RotateTokenRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Register Partner Service");

			// find partner object by code

			// generate access key

			// save it into db

			// return success response

			logger.info("Register of Partner in DB Success");
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info("Register Partner Success");
			logger.info(String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Register Partner Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Register Partner Service.", e);
			logger.info(String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Register Partner Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getAccessKeys(RotateTokenRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Register Partner Service");

			// fetch access keys from db based on partner code

			// return list of access keys

			logger.info("Register of Partner in DB Success");
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info("Register Partner Success");
			logger.info(String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Register Partner Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Register Partner Service.", e);
			logger.info(String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Register Partner Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateAccessKey(AccessKeyResponsePayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Register Partner Service");

			// fetch access key from db based on partner code

			// set access key status

			// save it into db

			// return response

			logger.info("Register of Partner in DB Success");
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info("Register Partner Success");
			logger.info(String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Register Partner Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Register Partner Service.", e);
			logger.info(String.format("Time taken on Register Partner Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Register Partner Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getUnassignedContracts(PartnersRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info("Start of get Unassigned Contracts Service : " + payload.getPartnerCode());
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Partner partner = partnersRepository.findByCode(payload.getPartnerCode());

			if (partner != null) {
				logger.info("Partner Recods Exist");

				List<Contract> contracts = contractRepository.getAllContractDetailsByStatus(PortalConstant.ACTIVE);

				List<PartnerContracts> partnerContracts = partner.getPartnerContracts();

				Set<Long> existingContractIds = partnerContracts.stream().filter(o -> o.isDeleted() == false)
						.map(o -> o.getContract().getId()).collect(Collectors.toSet());

				List<ContractDTO> unassignedContractDTOs = new ArrayList<ContractDTO>();

				for (Contract contract : contracts) {
					if (!existingContractIds.contains(contract.getId())) {
						ContractDTO contractDTO = new ContractDTO();
						contractDTO.setContractId(contract.getId().toString());
						contractDTO.setContractCode(contract.getCode());
						contractDTO.setContractName(contract.getContractName());
						unassignedContractDTOs.add(contractDTO);
					}
				}
				logger.info("Total Contracts count : " + contracts.size());
				logger.info("Total Assigned Contracts count : " + existingContractIds.size());
				logger.info("Total Unassigned Contracts count : " + unassignedContractDTOs.size());
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", unassignedContractDTOs);
				stopWatch.stop();
				logger.info("Get Unassigned Contracts Service Success");
				logger.info(String.format("Time taken on get Unassigned Contracts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Unassigned Contracts Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Unassigned Contracts Service failed");
				logger.info("Partners Not Found :- ");
				logger.info(String.format("Time taken on get Unassigned Contracts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of get Unassigned Contracts Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Unassigned Contracts Service.", e);
			logger.info(String.format("Time taken on get Unassigned Contracts Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Unassigned Contracts Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> assignContract(AssignContractRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Assign Contract Service");
			Partner partner = partnersRepository.findByCode(payload.getPartnerCode());
			if (partner != null) {
				List<Long> data = payload.getData();
				List<PartnerContracts> partnerContracts = new ArrayList<PartnerContracts>();
				for (Long contractId : data) {
					Contract contract = contractRepository.findById(contractId).orElse(null);
					if (contract != null) {
						PartnerContracts partnerContract = new PartnerContracts();
						partnerContract.setStatus(PortalConstant.ACTIVE);
						partnerContract.setDeleted(false);
						partnerContract.setCreatedBy(Long.parseLong(payload.getUserId()));
						partnerContract.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
						partnerContract.setPartner(partner);
						partnerContract.setContract(contract);
						partnerContract.setContractName(contract.getContractName());
						partnerContract.setUpdatedBy(Long.parseLong(payload.getUserId()));
						partnerContract.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
						partnerContracts.add(partnerContract);
					}
				}
				partnerContractsRepository.saveAll(partnerContracts);
				logger.info("Assign of Contract in DB Success");
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Assign Contract Success");
				logger.info(
						String.format("Time taken on Assign Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Assign Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Assign Contract failed");
				logger.info(String.format("Partner Not Found :-"));
				logger.info(
						String.format("Time taken on Assign Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Assign Contract Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Assign Contract Service.", e);
			logger.info(String.format("Time taken on Assign Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Assign Contract Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getPartnerContracts(PartnersRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info("Start of get Partner Contracts Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<PartnerContracts> partnerContracts = partnerContractsRepository
					.findByPartnerCode(payload.getPartnerCode(), pageRequest);

			if (partnerContracts != null && !partnerContracts.isEmpty()) {
				logger.info("Partner Contract Recods Exist");
				List<PartnerContractsDTO> partnerContractsDTOs = new ArrayList<PartnerContractsDTO>();
				for (PartnerContracts partnerContract : partnerContracts) {
					PartnerContractsDTO partnerContractsDTO = new PartnerContractsDTO();
					partnerContractsDTO.setPartnerContractId(partnerContract.getId().toString());
					partnerContractsDTO.setPartnerCode(partnerContract.getPartner().getCode());
					partnerContractsDTO.setContractCode(partnerContract.getContract().getCode());
					partnerContractsDTO.setContractName(partnerContract.getContractName());
					partnerContractsDTO.setStatus(partnerContract.getStatus());
					partnerContractsDTO.setDeleted(partnerContract.isDeleted() ? "true" : "false");
					partnerContractsDTO.setCreatedDate(partnerContract.getCreatedDate());
					if (partnerContract.getPartnerClientContract() != null
							&& !partnerContract.getPartnerClientContract().isEmpty()) {
						PartnerClientContract partnerClientContract = partnerContract.getPartnerClientContract().get(0);
						partnerContractsDTO.setClientId(partnerClientContract.getClientId());
					}
					partnerContractsDTOs.add(partnerContractsDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", partnerContractsDTOs);
				responseMap.put("count", partnerContracts.getTotalElements());
				stopWatch.stop();
				logger.info("Get Partner Contracts Service Success");
				logger.info(String.format("Time taken on get Partner Contracts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Partner Contracts Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Partner Contracts Service failed");
				logger.info("Partner Contracts Not Found :- ");
				logger.info(String.format("Time taken on get Partner Contracts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Partner Contracts Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Partner Contracts Service.", e);
			logger.info(String.format("Time taken on get Partner Contracts Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Partner Contracts Service.");
			return responseMap;
		}
	}

	@Override
	@Transactional
	public Map<Object, Object> updatePartnerContract(PartnerContractRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Partner Contracts Service");

			PartnerContracts partnerContractExist = partnerContractsRepository
					.findById(Long.parseLong(payload.getPartnerContractId())).orElse(null);

			if (partnerContractExist != null) {
				partnerContractExist.setStatus(payload.getStatus());
				partnerContractExist.setDeleted(Boolean.valueOf(payload.getDeleted()));
				partnerContractExist.setUpdatedBy(Long.parseLong(payload.getUserId()));
				partnerContractExist.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));

				if (payload.getClientId() != null && !payload.getClientId().isEmpty()) {
					logger.info("Need to Create or Update Record in Partner Client Contract");
					PartnerClientContract partnerClientContract = (partnerContractExist
							.getPartnerClientContract() == null
							|| partnerContractExist.getPartnerClientContract().isEmpty()) ? null
									: partnerContractExist.getPartnerClientContract().get(0);
					if (partnerClientContract != null) {
						logger.info("Partner Client Contract Exist");
						if (!partnerClientContract.getClientId().equals(payload.getClientId())) {
							if (checkForUniqueClientId(payload.getClientId())) {
								partnerClientContract.setClientId(payload.getClientId());
								partnerClientContract.setCreatedDate(DateUtils.getCurrentTimestamp());
								partnerClientContract.setUpdatedDate(DateUtils.getCurrentTimestamp());
								partnerClientContract.setStatus(payload.getStatus());
								partnerClientContract.setDeleted(Boolean.valueOf(payload.getDeleted()));
								partnerClientContractRepository.save(partnerClientContract);
								logger.info("Update Partner Client Contract Success");
							} else {
								responseMap.put("code", PortalConstant.OK);
								responseMap.put("message", CommonMessageLog.CLIENTIDALREADYEXIST);
								responseMap.put("status", CommonMessageLog.FAILED);
								stopWatch.stop();
								logger.info("Update Partner Contracts failed");
								logger.info(String.format("Client Id Already Exist :-"));
								logger.info(String.format("Time taken on Update Partner Contracts Service :- %s",
										stopWatch.getTotalTimeSeconds()));
								logger.info(String.format("End of Update Partner Contracts Service %s",
										CommonMessageLog.CLIENTIDALREADYEXIST));
								return responseMap;
							}
						}
					} else {
						logger.info("Partner Client Contract Not Exist");
						logger.info("Creating New Partner Client Contract");
						if (checkForUniqueClientId(payload.getClientId())) {
							partnerClientContract = new PartnerClientContract();
							partnerClientContract.setClientId(payload.getClientId());
							partnerClientContract.setPartnerContracts(partnerContractExist);
							partnerClientContract.setCreatedBy(Long.parseLong(payload.getUserId()));
							partnerClientContract.setUpdatedBy(Long.parseLong(payload.getUserId()));
							partnerClientContract.setCreatedDate(DateUtils.getCurrentTimestamp());
							partnerClientContract.setUpdatedDate(DateUtils.getCurrentTimestamp());
							partnerClientContract.setStatus(payload.getStatus());
							partnerClientContract.setDeleted(Boolean.valueOf(payload.getDeleted()));
							partnerClientContractRepository.save(partnerClientContract);
							logger.info("New Partner Client Contract Success");
						} else {
							responseMap.put("code", PortalConstant.OK);
							responseMap.put("message", CommonMessageLog.CLIENTIDALREADYEXIST);
							responseMap.put("status", CommonMessageLog.FAILED);
							stopWatch.stop();
							logger.info("Update Partner Contracts failed");
							logger.info(String.format("Client Id Already Exist :-"));
							logger.info(String.format("Time taken on Update Partner Contracts Service :- %s",
									stopWatch.getTotalTimeSeconds()));
							logger.info(String.format("End of Update Partner Contracts Service %s",
									CommonMessageLog.CLIENTIDALREADYEXIST));
							return responseMap;
						}
					}
				}
				partnerContractsRepository.save(partnerContractExist);
				logger.info("Updation of Partner Contracts in DB Success");
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Partner Contracts Success");
				logger.info(String.format("Time taken on Update Partner Contracts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Partner Contracts Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Partner Contracts failed");
				logger.info(String.format("Partner Contracts Not Exist :-"));
				logger.info(String.format("Time taken on Update Partner Contracts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Update Partner Contracts Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Partner Contracts Service.", e);
			logger.info(String.format("Time taken on Update Partner Contracts Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Contracts Service.");
			return responseMap;
		}
	}

	private boolean checkForUniqueClientId(String clientId) {
		boolean isUnique = partnerClientContractRepository.checkForUniqueClientId(clientId);
		return isUnique;
	}

	@Override
	public Map<Object, Object> partnerUploadFile(MultipartFile file, String fileType, String partnerCode, String name,
			String desc, String userId, String originalFileName) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Upload File service %s", partnerCode));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Partner partner = partnersRepository.findByCode(partnerCode);
			if (partner != null) {
				PartnerFilesUpload fileExist = partnersFileUploadRepository.findFileByFileNameAndPartnerCode(name,
						partnerCode);
				if (fileExist == null) {
					String bucketName = BUCKET_NAME;
					String fileNameDB = name;
					String originalFileNameDB = originalFileName;
					String generatedFileName = new Date().getTime() + "-" + originalFileName;
					String fileName = FirebaseScreen.PARTNERS + "/" + partnerCode + "/" + FirebaseScreen.FILES + "/"
							+ generatedFileName;
					String filePath = FirebaseScreen.GOOGLESTORAGE + bucketName + "/" + fileName;
					byte[] fileContent = file.getBytes();

					uploadFileToFirebase(fileName, fileContent, bucketName);

					PartnerFilesUpload partnerFilesUpload = new PartnerFilesUpload();
					partnerFilesUpload.setFileName(fileNameDB);
					partnerFilesUpload.setOriginalFileName(originalFileNameDB);
					partnerFilesUpload.setGeneratedFileName(generatedFileName);
					partnerFilesUpload.setFilePath(filePath);
					partnerFilesUpload.setFileType(fileType);
					partnerFilesUpload.setFileDesc(desc);
					partnerFilesUpload.setPartner(partner);
					partnerFilesUpload.setStatus(PortalConstant.ACTIVE);
					partnerFilesUpload.setDeleted(false);
					partnerFilesUpload.setReason(PortalConstant.SUCCESS);
					partnerFilesUpload.setCreatedBy(Long.parseLong(userId));
					partnerFilesUpload.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					partnerFilesUpload.setUpdatedBy(Long.parseLong(userId));
					partnerFilesUpload.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					partnersFileUploadRepository.save(partnerFilesUpload);

					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.FILEUPLOADSUCCESS);
					responseMap.put("status", CommonMessageLog.SUCCESS);
					stopWatch.stop();
					logger.info("Upload File Success");
					logger.info(
							String.format("Time taken on Upload File Service :- %s", stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Upload File Service %s", CommonMessageLog.SUCCESS));
					return responseMap;
				} else {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.FILENAMEALREADYEXIST);
					responseMap.put("status", CommonMessageLog.FAILED);
					stopWatch.stop();
					logger.info("Upload File failed");
					logger.info(
							String.format("Time taken on Upload File Service :- %s", stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Upload File Service %s", CommonMessageLog.FILENAMEALREADYEXIST));
					return responseMap;
				}
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Upload File failed");
				logger.info(String.format("Time taken on Upload File Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Upload File Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Upload File Service.", e);
			logger.info(String.format("Time taken on Upload File Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Upload File Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> partnerUploadLocalFile(MultipartFile file, String fileType, String partnerCode,
			String name, String desc, String userId, String originalFileName) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Upload Local File service %s", partnerCode));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Partner partner = partnersRepository.findByCode(partnerCode);
			if (partner != null) {
				String bucketName = BUCKET_NAME;
				String fileNameDB = name;
				String originalFileNameDB = originalFileName;
				String generatedFileName = new Date().getTime() + "-" + originalFileName;
				String fileName = FirebaseScreen.PARTNERS + "/" + partnerCode + "/" + FirebaseScreen.LOCALFILES + "/"
						+ generatedFileName;
				String filePath = FirebaseScreen.GOOGLESTORAGE + bucketName + "/" + fileName;
				byte[] fileContent = file.getBytes();

				uploadFileToFirebase(fileName, fileContent, bucketName);

				LocalFileUploadResponse response = new LocalFileUploadResponse();
				response.setDesc(desc);
				response.setFileName(fileNameDB);
				response.setFilePath(filePath);
				response.setOriginalFileName(originalFileNameDB);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.FILEUPLOADSUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", response);

				stopWatch.stop();
				logger.info("Upload Local File Success");
				logger.info(String.format("Time taken on Upload Local File Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Upload Local File Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Upload Local File failed");
				logger.info(String.format("Time taken on Upload Local File Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Upload Local File Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Upload Local File Service.", e);
			logger.info(
					String.format("Time taken on Upload Local File Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Upload Local File Service.");
			return responseMap;
		}
	}

	public void uploadFileToFirebase(String fileName, byte[] fileContent, String bucketName) throws IOException {
		cloudStorageService.uploadFile(bucketName, fileName, fileContent);
	}

	@Override
	public Map<Object, Object> getFileUploadList(String partnerCode, PageRequest page) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Upload List Service ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<PartnerFilesUpload> uploadedFiles = partnersFileUploadRepository.findByPartnerCode(partnerCode, page);
			if (uploadedFiles != null && !uploadedFiles.isEmpty()) {
				List<PartnerFileInfoDTO> partnerFileInfoDTOs = new ArrayList<PartnerFileInfoDTO>();
				for (PartnerFilesUpload uploadedFile : uploadedFiles) {
					PartnerFileInfoDTO partnerFileInfoDTO = new PartnerFileInfoDTO();
					partnerFileInfoDTO.setId(uploadedFile.getId().toString());
					partnerFileInfoDTO.setFileName(uploadedFile.getFileName());
					partnerFileInfoDTO.setStoragePath(uploadedFile.getFilePath());
					partnerFileInfoDTO.setOriginalFileName(uploadedFile.getOriginalFileName());
					partnerFileInfoDTO.setFileType(uploadedFile.getFileType());
					partnerFileInfoDTO.setFileDesc(uploadedFile.getFileDesc());
					partnerFileInfoDTO.setStatus(uploadedFile.getStatus());
					partnerFileInfoDTO.setDeleted(uploadedFile.isDeleted() ? "true" : "false");
					partnerFileInfoDTO.setCreatedDate(uploadedFile.getCreatedDate());
					partnerFileInfoDTOs.add(partnerFileInfoDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", partnerFileInfoDTOs);
				responseMap.put("count", uploadedFiles.getTotalElements());
				stopWatch.stop();
				logger.info("Get Upload List Success");
				logger.info(
						String.format("Time taken on Get Upload List Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Upload List Service %s", CommonMessageLog.SUCCESS));
				return responseMap;

			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList<>());
				stopWatch.stop();
				logger.info("Get Proof Upload List failed");
				logger.info(String.format("Records Not Found :- %s", partnerCode));
				logger.info(
						String.format("Time taken on Get Upload List Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Upload List Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Upload List Service.", e);
			logger.info(String.format("Time taken on Get Upload List Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Upload List Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateFileInfo(MultipartFile file, String fileType, String id, String userId,
			String status, String deleted, String name, String desc, String originalFileName) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update File Info Service");
			PartnerFilesUpload fileExist = partnersFileUploadRepository.findById(Long.parseLong(id)).orElse(null);
			if (fileExist != null) {
				if (!fileExist.getFileName().equals(name)) {
					PartnerFilesUpload fileNameExist = partnersFileUploadRepository.findFileByFileNameAndFileId(name,
							Long.parseLong(id));
					if (fileNameExist == null) {
						fileExist.setFileName(name);
					} else {
						responseMap.put("code", PortalConstant.OK);
						responseMap.put("message", CommonMessageLog.FILENAMEALREADYEXIST);
						stopWatch.stop();
						logger.info("Update File Info failed");
						logger.info(String.format("File Name Already Exist :-"));
						logger.info(String.format("Time taken on Update File Info Service :- %s",
								stopWatch.getTotalTimeSeconds()));
						logger.info(String.format("End of Update File Info Service %s",
								CommonMessageLog.FILENAMEALREADYEXIST));
						return responseMap;
					}
				}
				if (file != null && !file.isEmpty()) {
					String bucketName = BUCKET_NAME;
					String generatedFileName = new Date().getTime() + "-" + originalFileName;
					String fileName = FirebaseScreen.PARTNERS + "/" + fileExist.getPartner().getCode() + "/"
							+ FirebaseScreen.FILES + "/" + generatedFileName;
					String filePath = FirebaseScreen.GOOGLESTORAGE + bucketName + "/" + fileName;
					byte[] fileContent = file.getBytes();

					uploadFileToFirebase(fileName, fileContent, bucketName);
					fileExist.setFilePath(filePath);
					fileExist.setGeneratedFileName(generatedFileName);
				}
				fileExist.setOriginalFileName(originalFileName);
				fileExist.setFileType(fileType);
				fileExist.setFileDesc(desc);
				fileExist.setStatus(status);
				fileExist.setDeleted(Boolean.valueOf(deleted));
				fileExist.setUpdatedBy(Long.parseLong(userId));
				fileExist.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				partnersFileUploadRepository.save(fileExist);

				logger.info("Updation of Partner File Info in DB Success");
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Partner File Info Success");
				logger.info(String.format("Time taken on Update Partner File Info Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Partner File Info Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Partner File Info failed");
				logger.info(String.format("Partner File Info Not Exist :-"));
				logger.info(String.format("Time taken on Update Partner File Info Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Update Partner File Info Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Partner File Info Service.", e);
			logger.info(String.format("Time taken on Update Partner File Info Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner File Info Service.");
			return responseMap;
		}

	}

	@Override
	public Map<Object, Object> partnerDownloadFiles(List<Long> data) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Download Files service ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<DownloadFileInfoDataDTO> downloadFileInfoDataDTOs = new ArrayList<DownloadFileInfoDataDTO>();
			for (Long fileId : data) {
				PartnerFilesUpload fileExist = partnersFileUploadRepository.findById(fileId).orElse(null);
				if (fileExist != null) {
					DownloadFileInfoDataDTO downloadFileInfoDataDTO = new DownloadFileInfoDataDTO();
					downloadFileInfoDataDTO.setFileName(fileExist.getFileName());
					downloadFileInfoDataDTO.setOriginalFileName(fileExist.getOriginalFileName());
					downloadFileInfoDataDTO.setFileType(fileExist.getFileType());
					downloadFileInfoDataDTO.setId(fileId.toString());
					String bucketName = BUCKET_NAME;
					String objectName = FirebaseScreen.PARTNERS + "/" + fileExist.getPartner().getCode() + "/"
							+ FirebaseScreen.FILES + "/" + fileExist.getGeneratedFileName();
					String downloadLink = cloudStorageService.getSingedUrl(bucketName, objectName, 10);
					downloadFileInfoDataDTO.setDownloadLink(downloadLink);
					downloadFileInfoDataDTOs.add(downloadFileInfoDataDTO);
				}
			}
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			responseMap.put("data", downloadFileInfoDataDTOs);
			stopWatch.stop();
			logger.info("Download Files Success");
			logger.info(String.format("Time taken on Download Files Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Download Files Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Download Files Service.", e);
			logger.info(String.format("Time taken on Download Files Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Download Files Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> addConstants(PartnerConstantsRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Add Partner Constant Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Partner partner = partnersRepository.findByCode(payload.getPartnerCode());

			PartnerConstants constantExist = partnerConstantsRepository
					.findByPartnerCodeAndConstantName(payload.getPartnerCode(), payload.getConstantName());
			if (partner != null) {
				logger.info("Partner Recods Exist");
				if (constantExist == null) {
					PartnerConstants partnerConstant = new PartnerConstants();
					partnerConstant.setPartner(partner);
					partnerConstant.setConstantName(payload.getConstantName());
					partnerConstant.setConstantValue(payload.getConstantValue());
					partnerConstant.setConstantType(PortalConstant.STRING_TYPE);
					partnerConstant.setCreatedBy(Long.parseLong(payload.getUserId()));
					partnerConstant.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					partnerConstant.setUpdatedBy(Long.parseLong(payload.getUserId()));
					partnerConstant.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					partnerConstant.setStatus(PortalConstant.ACTIVE);
					partnerConstant.setDeleted(false);
					partnerConstantsRepository.save(partnerConstant);

					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.SUCCESS);
					responseMap.put("status", CommonMessageLog.SUCCESS);
					stopWatch.stop();
					logger.info("Add Partner Constant Service Success");
					logger.info(String.format("Time taken on Add Partner Constant Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Add Partner Constant Service %s", CommonMessageLog.SUCCESS));
					return responseMap;
				} else {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.ConstantAlreadyExist);
					responseMap.put("status", CommonMessageLog.FAILED);
					stopWatch.stop();
					logger.info("Add Partner Constant Service failed");
					logger.info("Partner Constant Already Exist :- ");
					logger.info(String.format("Time taken on Add Partner Constant Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Add Partner Constant Service %s",
							CommonMessageLog.ConstantAlreadyExist));
					return responseMap;
				}
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Add Partner Constant Service failed");
				logger.info("Partner Not Found :- ");
				logger.info(String.format("Time taken on Add Partner Constant Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Partner Constant Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add Partner Constant Service.", e);
			logger.info(
					String.format("Time taken on Add Partner Constant Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner Constant Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getConstants(PartnersRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Partner Constant Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<PartnerConstants> constants = partnerConstantsRepository.findByPartnerCode(payload.getPartnerCode(),
					pageRequest);
			if (constants != null && !constants.isEmpty()) {
				logger.info("Partner Constants Recods Exist");
				List<PartnerConstantsDTO> partnerConstantsDTOs = new ArrayList<PartnerConstantsDTO>();
				for (PartnerConstants constant : constants) {
					PartnerConstantsDTO partnerConstantsDTO = new PartnerConstantsDTO();
					partnerConstantsDTO.setId(constant.getId().toString());
					partnerConstantsDTO.setConstantName(constant.getConstantName());
					partnerConstantsDTO.setConstantValue(constant.getConstantValue());
					partnerConstantsDTO.setStatus(constant.getStatus());
					partnerConstantsDTO.setDeleted(constant.isDeleted() ? "true" : "false");
					partnerConstantsDTOs.add(partnerConstantsDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", partnerConstantsDTOs);
				responseMap.put("count", constants.getTotalElements());
				stopWatch.stop();
				logger.info("Get Partner Constant Service Success");
				logger.info(String.format("Time taken on Get Partner Constant Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner Constant Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				responseMap.put("count", 0);
				stopWatch.stop();
				logger.info("Get Partner Constant Service failed");
				logger.info(String.format("Time taken on Add Partner Constant Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner Constant Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Partner Constant Service.", e);
			logger.info(
					String.format("Time taken on Get Partner Constant Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Constant Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateConstants(PartnerConstantsDTO payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Update Partner Constant Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			PartnerConstants constantExist = partnerConstantsRepository.findById(Long.parseLong(payload.getId()))
					.orElse(null);
			if (constantExist != null) {
				logger.info("Partner Constant Recods Exist");
				constantExist.setConstantValue(payload.getConstantValue());
				constantExist.setStatus(payload.getStatus());
				constantExist.setDeleted(Boolean.valueOf(payload.getDeleted()));
				constantExist.setUpdatedBy(Long.parseLong(payload.getUserId()));
				constantExist.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				partnerConstantsRepository.save(constantExist);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Partner Constant Service Success");
				logger.info(String.format("Time taken on Update Partner Constant Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Partner Constant Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Partner Constant Service failed");
				logger.info("constant Not Found :- ");
				logger.info(String.format("Time taken on Update Partner Constant Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Update Partner Constant Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Partner Constant Service.", e);
			logger.info(String.format("Time taken on Update Partner Constant Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Constant Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> addContacts(PartnerContactsRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Add Partner Contacts Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Partner partner = partnersRepository.findByCode(payload.getPartnerCode());
			if (partner != null) {
				logger.info("Partner Recods Exist");
				PartnerContacts contact = new PartnerContacts();
				contact.setPartner(partner);
				contact.setName(payload.getName());
				contact.setDesignation(payload.getDesignation());
				contact.setAbout(payload.getAbout());
				contact.setPhone(payload.getPhone());
				contact.setEmail(payload.getEmail());
				contact.setOwnership(payload.getOwnership());
				contact.setCreatedBy(Long.parseLong(payload.getUserId()));
				contact.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				contact.setUpdatedBy(Long.parseLong(payload.getUserId()));
				contact.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				contact.setStatus(PortalConstant.ACTIVE);
				contact.setDeleted(false);
				partnerContactsRepository.save(contact);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Add Partner Contacts Service Success");
				logger.info(String.format("Time taken on Add Partner Contacts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Partner Contacts Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Add Partner Contacts Service failed");
				logger.info("Partner Not Found :- ");
				logger.info(String.format("Time taken on Add Partner Contacts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Partner Contacts Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add Partner Contacts Service.", e);
			logger.info(
					String.format("Time taken on Add Partner Contacts Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner Contacts Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getContacts(PartnersRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Partner Contacts Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<PartnerContacts> contacts = partnerContactsRepository.findByPartnerCode(payload.getPartnerCode(),
					pageRequest);
			if (contacts != null && !contacts.isEmpty()) {
				logger.info("Partner Contacts Recods Exist");
				List<PartnerContactsDTO> partnerContactsDTOs = new ArrayList<PartnerContactsDTO>();
				for (PartnerContacts contact : contacts) {
					PartnerContactsDTO contactsDTO = new PartnerContactsDTO();
					contactsDTO.setId(contact.getId().toString());
					contactsDTO.setName(contact.getName());
					contactsDTO.setDesignation(contact.getDesignation());
					contactsDTO.setAbout(contact.getAbout());
					contactsDTO.setPhone(contact.getPhone());
					contactsDTO.setOwnership(contact.getOwnership());
					contactsDTO.setStatus(contact.getStatus());
					contactsDTO.setDeleted(contact.isDeleted() ? "true" : "false");
					contactsDTO.setUserId(contact.getCreatedBy().toString());
					contactsDTO.setEmail(contact.getEmail());
					partnerContactsDTOs.add(contactsDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", partnerContactsDTOs);
				responseMap.put("count", contacts.getTotalElements());
				stopWatch.stop();
				logger.info("Get Partner Contacts Service Success");
				logger.info(String.format("Time taken on Get Partner Contacts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner Contacts Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				responseMap.put("count", 0);
				stopWatch.stop();
				logger.info("Get Partner Contacts Service failed");
				logger.info(String.format("Time taken on Add Partner Contacts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner Contacts Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Partner Contacts Service.", e);
			logger.info(
					String.format("Time taken on Get Partner Contacts Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Contacts Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateContacts(PartnerContactsDTO payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Update Partner Contacts Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			PartnerContacts contactExist = partnerContactsRepository.findById(Long.parseLong(payload.getId()))
					.orElse(null);
			if (contactExist != null) {
				logger.info("Partner Contacts Recods Exist");
				contactExist.setAbout(payload.getAbout());
				contactExist.setDesignation(payload.getDesignation());
				contactExist.setName(payload.getName());
				contactExist.setPhone(payload.getPhone());
				contactExist.setOwnership(payload.getOwnership());
				contactExist.setEmail(payload.getEmail());
				contactExist.setStatus(payload.getStatus());
				contactExist.setDeleted(Boolean.valueOf(payload.getDeleted()));
				contactExist.setUpdatedBy(Long.parseLong(payload.getUserId()));
				contactExist.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				partnerContactsRepository.save(contactExist);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Partner Contacts Service Success");
				logger.info(String.format("Time taken on Update Partner Contacts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Partner Contacts Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Partner Contacts Service failed");
				logger.info("Contacts Not Found :- ");
				logger.info(String.format("Time taken on Update Partner Contacts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Update Partner Contacts Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Partner Contacts Service.", e);
			logger.info(String.format("Time taken on Update Partner Contacts Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Contacts Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> addNotes(PartnerNotesRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Add Partner Notes Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Partner partner = partnersRepository.findByCode(payload.getPartnerCode());
			if (partner != null) {
				logger.info("Partner Recods Exist");
				PartnerNotes note = new PartnerNotes();
				note.setPartner(partner);
				note.setDescription(payload.getDesc());
				note.setCreatedBy(Long.parseLong(payload.getUserId()));
				note.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				note.setUpdatedBy(Long.parseLong(payload.getUserId()));
				note.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				note.setStatus(PortalConstant.ACTIVE);
				note.setDeleted(false);
				partnerNotesRepository.save(note);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Add Partner Notes Service Success");
				logger.info(String.format("Time taken on Add Partner Notes Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Partner Notes Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Add Partner Notes Service failed");
				logger.info("Partner Not Found :- ");
				logger.info(String.format("Time taken on Add Partner Notes Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Partner Notes Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add Partner Notes Service.", e);
			logger.info(
					String.format("Time taken on Add Partner Notes Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner Notes Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getNotes(PartnersRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Partner Notes Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<PartnerNotes> notes = partnerNotesRepository.findByPartnerCode(payload.getPartnerCode(), pageRequest);
			if (notes != null && !notes.isEmpty()) {
				logger.info("Partner Notes Recods Exist");
				List<PartnerNotesDTO> partnerNotesDTOs = new ArrayList<PartnerNotesDTO>();
				for (PartnerNotes note : notes) {
					PartnerNotesDTO notesDTO = new PartnerNotesDTO();
					notesDTO.setId(note.getId().toString());
					notesDTO.setDesc(note.getDescription());
					notesDTO.setStatus(note.getStatus());
					notesDTO.setDeleted(note.isDeleted() ? "true" : "false");
					notesDTO.setCreatedDate(note.getCreatedDate());
					partnerNotesDTOs.add(notesDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", partnerNotesDTOs);
				responseMap.put("count", notes.getTotalElements());
				stopWatch.stop();
				logger.info("Get Partner Notes Service Success");
				logger.info(String.format("Time taken on Get Partner Notes Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner Notes Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				responseMap.put("count", 0);
				stopWatch.stop();
				logger.info("Get Partner Notes Service failed");
				logger.info(String.format("Time taken on Add Partner Notes Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner Notes Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Partner Notes Service.", e);
			logger.info(
					String.format("Time taken on Get Partner Notes Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Notes Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateNotes(PartnerNotesDTO payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Update Partner Notes Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			PartnerNotes notesExist = partnerNotesRepository.findById(Long.parseLong(payload.getId())).orElse(null);
			if (notesExist != null) {
				logger.info("Partner Notes Recods Exist");
				notesExist.setDescription(payload.getDesc());
				notesExist.setStatus(payload.getStatus());
				notesExist.setDeleted(Boolean.valueOf(payload.getDeleted()));
				notesExist.setUpdatedBy(Long.parseLong(payload.getUserId()));
				notesExist.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				partnerNotesRepository.save(notesExist);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Partner Notes Service Success");
				logger.info(String.format("Time taken on Update Partner Notes Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Partner Notes Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Partner Notes Service failed");
				logger.info("Contacts Not Found :- ");
				logger.info(String.format("Time taken on Update Partner Notes Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Partner Notes Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Partner Notes Service.", e);
			logger.info(
					String.format("Time taken on Update Partner Notes Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Notes Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> addAPIs(PartnerAPIsRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Add Partner APIs Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Partner partner = partnersRepository.findByCode(payload.getPartnerCode());
			if (partner != null) {
				logger.info("Partner Recods Exist");
				PartnerApis api = new PartnerApis();
				api.setPartner(partner);
				api.setApiName(payload.getApiName());
				api.setApiKeyRequired(payload.getApiKeyRequired());
				api.setApiOwner(payload.getApiOwner());
				api.setAuthTokenRequired(payload.getAuthTokenRequired());
				api.setIsInternal(payload.getIsInternal());
				api.setCreatedBy(Long.parseLong(payload.getUserId()));
				api.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				api.setUpdatedBy(Long.parseLong(payload.getUserId()));
				api.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				api.setStatus(PortalConstant.ACTIVE);
				api.setDeleted(false);
				partnerAPIsRepository.save(api);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Add Partner APIs Service Success");
				logger.info(
						String.format("Time taken on Add Partner APIs Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Partner APIs Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Add Partner APIs Service failed");
				logger.info("Partner Not Found :- ");
				logger.info(
						String.format("Time taken on Add Partner APIs Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Partner APIs Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add Partner APIs Service.", e);
			logger.info(String.format("Time taken on Add Partner APIs Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner APIs Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getAPIs(PartnersRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Partner APIs Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<PartnerApis> apis = partnerAPIsRepository.findByPartnerCode(payload.getPartnerCode(), pageRequest);
			if (apis != null && !apis.isEmpty()) {
				logger.info("Partner APIs Recods Exist");
				List<PartnerAPIsDTO> partnerAPIsDTOs = new ArrayList<PartnerAPIsDTO>();
				for (PartnerApis api : apis) {
					PartnerAPIsDTO apiDTO = new PartnerAPIsDTO();
					apiDTO.setId(api.getId().toString());
					apiDTO.setApiName(api.getApiName());
					apiDTO.setApiKeyRequired(api.getApiKeyRequired());
					apiDTO.setApiOwner(api.getApiOwner());
					apiDTO.setAuthTokenRequired(api.getAuthTokenRequired());
					apiDTO.setIsInternal(api.getIsInternal());
					apiDTO.setStatus(api.getStatus());
					apiDTO.setDeleted(api.isDeleted() ? "true" : "false");
					partnerAPIsDTOs.add(apiDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", partnerAPIsDTOs);
				responseMap.put("count", apis.getTotalElements());
				stopWatch.stop();
				logger.info("Get Partner APIs Service Success");
				logger.info(
						String.format("Time taken on Get Partner APIs Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner APIs Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				responseMap.put("count", 0);
				stopWatch.stop();
				logger.info("Get Partner APIs Service failed");
				logger.info(
						String.format("Time taken on Add Partner APIs Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner APIs Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Partner APIs Service.", e);
			logger.info(String.format("Time taken on Get Partner APIs Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner APIs Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> addTestCase(APITestCaseRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Add Partner APIs Test Case Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			PartnerApis partnerAPI = partnerAPIsRepository.findById(Long.parseLong(payload.getApiId())).orElse(null);
			if (partnerAPI != null) {
				logger.info("Partner APIS Recods Exist");

				PartnerApisTestCases testCaseExist = partnerAPIsTestCaseRepository.findByUUID(payload.getUuid());
				String bucketName = BUCKET_NAME;
				String apiIdFS = FirebaseScreen.API_ID_ + payload.getApiId();
				if (testCaseExist != null) {
					logger.info("UUID Already Exist");
					logger.info("Update Partner APIs Test Case : ");

					String fileName = FirebaseScreen.PARTNERS + "/" + payload.getPartnerCode() + "/"
							+ FirebaseScreen.APIS + "/" + apiIdFS + "/" + payload.getUuid();
					byte[] fileContent = StringUtil.pojoToString(payload.getJson()).getBytes(StandardCharsets.UTF_8);

					uploadFileToFirebase(fileName, fileContent, bucketName);

					testCaseExist.setTestCaseName(payload.getTestCaseName());
					testCaseExist.setStatus(payload.getStatus());
					testCaseExist.setDeleted(Boolean.valueOf(payload.getDeleted()));
					testCaseExist.setUpdatedBy(Long.parseLong(payload.getUserId()));
					testCaseExist.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					partnerAPIsTestCaseRepository.save(testCaseExist);
				} else {
					logger.info("UUID Not Exist");
					logger.info("Add Partner APIs Test Case : ");
					String fileName = FirebaseScreen.PARTNERS + "/" + payload.getPartnerCode() + "/"
							+ FirebaseScreen.APIS + "/" + apiIdFS + "/" + payload.getUuid();
					String filePath = FirebaseScreen.GOOGLESTORAGE + bucketName + "/" + fileName;
					byte[] fileContent = StringUtil.pojoToString(payload.getJson()).getBytes();

					uploadFileToFirebase(fileName, fileContent, bucketName);
					PartnerApisTestCases testCase = new PartnerApisTestCases();
					testCase.setPartnerApis(partnerAPI);
					testCase.setUuidV(payload.getUuid());
					testCase.setIndexV(payload.getIndex());
					testCase.setTestCaseName(payload.getTestCaseName());
					testCase.setPath(filePath);
					testCase.setStatus(payload.getStatus());
					testCase.setDeleted(Boolean.valueOf(payload.getDeleted()));
					testCase.setCreatedBy(Long.parseLong(payload.getUserId()));
					testCase.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					testCase.setUpdatedBy(Long.parseLong(payload.getUserId()));
					testCase.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					partnerAPIsTestCaseRepository.save(testCase);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Add Partner APIs Test Case Service Success");
				logger.info(String.format("Time taken on Add Partner APIs Test Case Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Partner APIs Test Case Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Add Partner APIs Test Case Service failed");
				logger.info("Partner API Not Found :- ");
				logger.info(String.format("Time taken on Add Partner APIs Test Case Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Partner APIs Test Case Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add Partner APIs Test Case Service.", e);
			logger.info(String.format("Time taken on Add Partner APIs Test Case Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner APIs Test Case Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getAPITestCases(PartnerAPIRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Partner APIs Test Cases Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<PartnerApisTestCases> testCases = partnerAPIsTestCaseRepository.findByPartnerApiId(payload.getApiId());
			if (testCases != null && !testCases.isEmpty()) {
				logger.info("Partner APIs Test Cases Recods Exist");
				List<PartnerAPIsTestCaseDTO> partnerAPIsDTOs = new ArrayList<PartnerAPIsTestCaseDTO>();
				for (PartnerApisTestCases testCase : testCases) {
					PartnerAPIsTestCaseDTO testCaseDTO = new PartnerAPIsTestCaseDTO();
					testCaseDTO.setId(testCase.getId().toString());
					testCaseDTO.setUuid(testCase.getUuidV());
					testCaseDTO.setIndex(testCase.getIndexV());
					testCaseDTO.setTestCaseName(testCase.getTestCaseName());
					testCaseDTO.setStatus(testCase.getStatus());
					testCaseDTO.setDeleted(testCase.isDeleted() ? "true" : "false");
					String apiIdFS = FirebaseScreen.API_ID_ + testCase.getPartnerApis().getId();
					String partnerCode = testCase.getPartnerApis().getPartner().getCode();
					String objectName = FirebaseScreen.PARTNERS + "/" + partnerCode + "/" + FirebaseScreen.APIS + "/"
							+ apiIdFS + "/" + testCase.getUuidV();
					byte[] readAllBytes = cloudStorageService.readAllBytes(objectName);
					String jsonString = new String(readAllBytes);
					if (jsonString != null) {
						testCaseDTO.setJson(StringUtil.jsonStringToObject(jsonString));
					}
					partnerAPIsDTOs.add(testCaseDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", partnerAPIsDTOs);
				stopWatch.stop();
				logger.info("Get Partner APIs Test Cases Service Success");
				logger.info(String.format("Time taken on Get Partner APIs Test Cases Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner APIs Test Cases Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Partner APIs Test Cases Service failed");
				logger.info(String.format("Time taken on Add Partner APIs Test Cases Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner APIs Test Cases Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Partner APIs Test Cases Service.", e);
			logger.info(String.format("Time taken on Get Partner APIs Test Cases Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner APIs Test Cases Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> executeTestCase(ExecuteTestCaseRequestPayload payload, String token) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Add Partner APIs Test Case Execute Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			PartnerApisTestCases testCaseExist = partnerAPIsTestCaseRepository
					.findById(Long.parseLong(payload.getTestCaseId())).orElse(null);
			if (testCaseExist != null) {
				logger.info("Test Case Exist");
				PartnerApiTestCaseExecution execution = new PartnerApiTestCaseExecution();
				execution.setPartnerApisTestCases(testCaseExist);
				execution.setCreatedBy(payload.getUserId());
				execution.setCreatedDate(DateUtils.getCurrentTimestamp());
				execution.setUpdatedBy(payload.getUserId());
				execution.setUpdatedDate(DateUtils.getCurrentTimestamp());
				execution.setStatus(PortalConstant.PENDING);
				UUID uuid = UUID.randomUUID();
				execution.setUuid(uuid.toString());
				execution = partnerApiTestCaseExecutionRepository.save(execution);
				logger.info("Test Case Execution Saved in DB Successfully");

				TestCaseExecutionPayload execute = new TestCaseExecutionPayload();
				execute.setUuid(execution.getUuid());
				execute.setJson(payload.getJson());

				logger.info("Calling publish method to publish Test Case Execution");
				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Type", "application/json");
				header.put("X-API-KEY", runnerXApiKey);
				header.put("Authorization", token);
				String publishMessage = StringUtil.pojoToString(execute);
				logger.info("Message to be published : " + publishMessage);
				cloudPubSubService.publishMessage(PROJECT_ID, publishTestCaseExecuteTopicId, header, publishMessage);
				logger.info("Publish Success ? : " + true);
				logger.info("Completion of pubsub call");

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.TESTCASEEXECUTIONUNDERPROCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("uuid", execution.getUuid());
				stopWatch.stop();
				logger.info("Add Partner APIs Test Case Execute Service Success");
				logger.info(String.format("Time taken on Add Partner APIs Test Case Execute Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Partner APIs Test Case Execute Service %s",
						CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Add Partner APIs Test Case Execute Service failed");
				logger.info("Partner API Test Case Not Found :- ");
				logger.info(String.format("Time taken on Add Partner APIs Test Case Execute Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Partner APIs Test Case Execute Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add Partner APIs Test Case Execute Service.", e);
			logger.info(String.format("Time taken on Add Partner APIs Test Case Execute Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner APIs Test Case Execute Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getTestCaseExecutions(TestCaseExecutionsRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Partner APIs Test Cases Executions Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<PartnerApiTestCaseExecution> executions = partnerApiTestCaseExecutionRepository
					.findByTestCaseId(Long.parseLong(payload.getTestCaseId()));
			if (executions != null && !executions.isEmpty()) {
				logger.info("Partner APIs Test Cases Executions Recods Exist");
				List<UpdateTestCaseExecutionRequestPayload> executionDTOs = new ArrayList<UpdateTestCaseExecutionRequestPayload>();
				for (PartnerApiTestCaseExecution execution : executions) {
					UpdateTestCaseExecutionRequestPayload executionDTO = new UpdateTestCaseExecutionRequestPayload();
					executionDTO.setLogPath(execution.getLogPath());
					executionDTO.setRespPath(execution.getRespPath());
					executionDTO.setStatus(execution.getStatus());
					executionDTO.setUuid(execution.getUuid());
					executionDTO.setCreatedDate(execution.getCreatedDate());
					executionDTO.setUpdatedDate(execution.getUpdatedDate());
					executionDTOs.add(executionDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", executionDTOs);
				stopWatch.stop();
				logger.info("Get Partner APIs Test Cases Executions Service Success");
				logger.info(String.format("Time taken on Get Partner APIs Test Cases Executions Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner APIs Test Cases Executions Service %s",
						CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Partner APIs Test Cases Service failed");
				logger.info(String.format("Time taken on Add Partner APIs Test Cases Executions Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Partner APIs Test Cases Executions Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Partner APIs Test Cases Executions Service.", e);
			logger.info(String.format("Time taken on Get Partner APIs Test Cases Executions Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner APIs Test Cases Executions Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> downloadExecutionFiles(String path) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Download Execution Files service ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			DownloadExecutionFileInfoDataDTO downloadData = new DownloadExecutionFileInfoDataDTO();
			String bucketName = getBucketName(path);
			String objectName = getObjectName(path);
			String downloadLink = cloudStorageService.getSingedUrl(bucketName, objectName, 10);
			downloadData.setSignedURL(downloadLink);
			downloadData.setPath(path);
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			responseMap.put("data", downloadData);
			stopWatch.stop();
			logger.info("Download Execution Files Success");
			logger.info(String.format("Time taken on Download Execution Files Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Download Execution Files Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Download Execution Files Service.", e);
			logger.info(String.format("Time taken on Download Execution Files Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Download Execution Files Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> fetchExecutionStatus(String uuid) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Fetch Execution Status Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			PartnerApiTestCaseExecution execution = partnerApiTestCaseExecutionRepository.findByUUID(uuid);
			if (execution != null) {
				logger.info("Partner APIs Test Cases Execution Recods Exist");
				ExecutionStatusResponsePayload status = new ExecutionStatusResponsePayload();
				status.setStatus(execution.getStatus());
				status.setUuid(execution.getUuid());
				if (!execution.getStatus().equals(PortalConstant.PENDING)) {
					String logPathBucketName = getBucketName(execution.getLogPath());
					String logPathObjectName = getObjectName(execution.getLogPath());
					String logPathSignedURL = cloudStorageService.getSingedUrl(logPathBucketName, logPathObjectName,
							10);
					String responsePathBucketName = getBucketName(execution.getRespPath());
					String responsePathObjectName = getObjectName(execution.getRespPath());
					String responsePathSignedURL = cloudStorageService.getSingedUrl(responsePathBucketName,
							responsePathObjectName, 10);
					status.setLogPathSignedURL(logPathSignedURL);
					status.setRespPathSignedURL(responsePathSignedURL);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", status);
				stopWatch.stop();
				logger.info("Fetch Execution Status Service Success");
				logger.info(String.format("Time taken on Fetch Execution Status Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Fetch Execution Status Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("Fetch Execution Status Service failed");
				logger.info(String.format("Time taken on Fetch Execution Status Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Fetch Execution Status Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Fetch Execution Status Service.", e);
			logger.info(String.format("Time taken on Fetch Execution Status Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Fetch Execution Status Service.");
			return responseMap;
		}
	}

	public String getBucketName(String url) {
		String withoutPrefix = url.substring(5);
		int slashIndex = withoutPrefix.indexOf('/');
		String bucketName = withoutPrefix.substring(0, slashIndex);
		logger.info("Bucket Name :- " + bucketName);
		return bucketName;
	}

	public String getObjectName(String url) {
		String withoutPrefix = url.substring(5);
		int slashIndex = withoutPrefix.indexOf('/');
		String objectName = withoutPrefix.substring(slashIndex + 1, withoutPrefix.length());
		logger.info("Object Name :- " + objectName);
		return objectName;
	}
}
