package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.instape.app.cloudsql.model.BankMaster;
import com.instape.app.cloudsql.model.BankMasterDTO;
import com.instape.app.cloudsql.model.Contract;
import com.instape.app.cloudsql.model.ContractAuditLogs;
import com.instape.app.cloudsql.model.ContractDataDTO;
import com.instape.app.cloudsql.model.ContractDetailDTO;
import com.instape.app.cloudsql.model.CustomerMaster;
import com.instape.app.cloudsql.model.CustomerMasterDTO;
import com.instape.app.cloudsql.model.LenderEndPointsDTO;
import com.instape.app.cloudsql.model.LenderEndpointsConfig;
import com.instape.app.cloudsql.repository.BankMasterRepository;
import com.instape.app.cloudsql.repository.ContractAuditLogsRepository;
import com.instape.app.cloudsql.repository.ContractRepository;
import com.instape.app.cloudsql.repository.CustomerMasterRepository;
import com.instape.app.cloudsql.repository.LenderEndpointsConfigRepository;
import com.instape.app.cloudstore.service.CloudPubSubService;
import com.instape.app.cloudstore.service.CloudTenantService;
import com.instape.app.firebase.service.FirestoreServiceHandler;
import com.instape.app.request.ContractDetailRequestPayload;
import com.instape.app.request.ContractDetailsRequestPayload;
import com.instape.app.request.ContractRequestPayload;
import com.instape.app.request.CreateContractRequestPayload;
import com.instape.app.request.CreateCustomerRequestPayload;
import com.instape.app.request.CustomerDetailsRequestPayload;
import com.instape.app.request.LenderEndPointsRequestPayload;
import com.instape.app.request.UpdateCustomerRequestPayload;
import com.instape.app.response.CustomerDetailsResponsePayload;
import com.instape.app.service.AuditService;
import com.instape.app.service.ContractDetailService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.FirebaseScreen;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 15-Jan-2024
 * @ModifyDate - 15-Jan-2024
 * @Desc -
 */
@Service
public class ContractDetailServiceImpl implements ContractDetailService {

	static final Logger logger = LogManager.getFormatterLogger(ContractDetailServiceImpl.class);

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private CustomerMasterRepository customerMasterRepository;

	@Autowired
	private BankMasterRepository bankMasterRepository;

	@Autowired
	private FirestoreServiceHandler firestoreServiceHandler;

	@Value("${FIRESTORE_ROOT}")
	private String firestoreRoot;

	@Autowired
	private ContractAuditLogsRepository contractAuditLogsRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CloudTenantService cloudTenantService;

	@Autowired
	private LenderEndpointsConfigRepository lenderEndpointsConfigRepository;

	@Autowired
	private AuditService auditService;

	@Value("${PROJECT_ID}")
	private String projectId;

	private String lenderEndPointTopicId = "update-lender-endpoints-queue";

	@Autowired
	private CloudPubSubService cloudPubSubService;

	@Override
	public Map<Object, Object> createContract(CreateContractRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Create Contract Service");

			CustomerMaster customerMaster = customerMasterRepository.findById(Long.parseLong(payload.getCustomerId()))
					.orElse(null);

			BankMaster bankMaster = bankMasterRepository.findById(Long.parseLong(payload.getLenderId())).orElse(null);

			if (customerMaster != null && bankMaster != null) {
				String fullContractCode = cloudTenantService.createTenant(payload.getContractCode());
				if (fullContractCode != null && !fullContractCode.isEmpty()) {
					logger.info("Full Contract Code Generation from Tenants Success");
					Contract contractExist = contractRepository.findByCode(fullContractCode);
					if (contractExist == null) {

						Map<String, Object> tenantDoc = null;
						tenantDoc = new HashMap<>();
						tenantDoc.put(PortalConstant.ID, fullContractCode);
						tenantDoc.put(FirebaseScreen.TIMESTAMP,
								Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

						firestoreServiceHandler.createFSDocument(tenantDoc, FirebaseScreen.TENANTS,
								customerMaster.getCode());

						logger.info("Creation of Tenant Document in Firestore Success");

						Contract contract = new Contract();
						contract.setCode(fullContractCode);
						contract.setStatus(PortalConstant.ACTIVE);
						contract.setBankMaster(bankMaster);
						contract.setCustomerMaster(customerMaster);
						contract.setCreatedBy(payload.getUserId());
						contract.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
						contract.setUpdatedBy(payload.getUserId());
						contract.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
						contractRepository.save(contract);

						logger.info("Creation of Contract in DB Success");

						ContractAuditLogs updateContractAudit = modelMapper.map(contract, ContractAuditLogs.class);
						updateContractAudit.setId(null);
						contractAuditLogsRepository.save(updateContractAudit);

						logger.info("Creation of Contract Audit Logs in DB Success");

						responseMap.put("code", PortalConstant.OK);
						responseMap.put("message", CommonMessageLog.SUCCESS);
						responseMap.put("status", CommonMessageLog.SUCCESS);
						stopWatch.stop();
						logger.info("Create Contract Success");
						logger.info(String.format("Time taken on Create Contract Service :- %s",
								stopWatch.getTotalTimeSeconds()));
						logger.info(String.format("End of Create Contract Service %s", CommonMessageLog.SUCCESS));
						return responseMap;
					} else {
						responseMap.put("code", PortalConstant.OK);
						responseMap.put("message", CommonMessageLog.CONTRACTALREADYEXIST);
						responseMap.put("status", CommonMessageLog.FAILED);
						stopWatch.stop();
						logger.info("Create Contract failed");
						logger.info(String.format("Contract Code Already Exist :-"));
						logger.info(String.format("Time taken on Create Contract Service :- %s",
								stopWatch.getTotalTimeSeconds()));
						logger.info(String.format("End of Create Contract Service %s",
								CommonMessageLog.CONTRACTALREADYEXIST));
						return responseMap;
					}
				} else {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.SomeThingWentWrong);
					responseMap.put("status", CommonMessageLog.FAILED);
					stopWatch.stop();
					logger.info("Create Contract failed");
					logger.info(String.format("Full Contract Code Generation from Tenants Failed :-"));
					logger.info(String.format("Time taken on Create Contract Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(
							String.format("End of Create Contract Service %s", CommonMessageLog.SomeThingWentWrong));
					return responseMap;
				}
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.BANKMASTERORCUSTOMERNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Create Contract failed");
				logger.info(String.format("Bank Master or Customer Master Not Found :-"));
				logger.info(
						String.format("Time taken on Create Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Create Contract Service %s",
						CommonMessageLog.BANKMASTERORCUSTOMERNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Create Contract Service.", e);
			logger.info(String.format("Time taken on Create Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Contract Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getCustomers() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Get Customers Service");
			List<CustomerMaster> customers = customerMasterRepository.getCustomerMastersByStatus(PortalConstant.ACTIVE);
			if (customers != null && !customers.isEmpty()) {
				List<CustomerMasterDTO> customerMasterDTOs = new ArrayList<CustomerMasterDTO>();
				for (CustomerMaster customerMaster : customers) {
					CustomerMasterDTO customerMasterDTO = new CustomerMasterDTO();
					customerMasterDTO.setId(customerMaster.getId().toString());
					customerMasterDTO.setName(customerMaster.getCustomerName());
					customerMasterDTO.setCode(customerMaster.getCode());
					customerMasterDTOs.add(customerMasterDTO);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", customerMasterDTOs);
				stopWatch.stop();
				logger.info("Get Customers Success");
				logger.info(
						String.format("Time taken on Get Customers Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Customers Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CustomerNotFound);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", null);
				stopWatch.stop();
				logger.info("Get Customers failed");
				logger.info(String.format("Customers Not Found :-"));
				logger.info(
						String.format("Time taken on Get Customers Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Customers Service %s", CommonMessageLog.CustomerNotFound));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Customers Service.", e);
			logger.info(String.format("Time taken on Get Customers Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Customers Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getLenders() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Get Bank Masters Service");
			List<BankMaster> bankMasters = bankMasterRepository.getAllActiveBankMaster(PortalConstant.INACTIVE);
			if (bankMasters != null && !bankMasters.isEmpty()) {
				List<BankMasterDTO> bankMasterDTOs = new ArrayList<BankMasterDTO>();
				for (BankMaster bankMaster : bankMasters) {
					BankMasterDTO bankMasterDTO = new BankMasterDTO();
					bankMasterDTO.setId(bankMaster.getId().toString());
					bankMasterDTO.setName(bankMaster.getBankName());
					bankMasterDTO.setCode(bankMaster.getCode());
					bankMasterDTO.setCinNumber(bankMaster.getCinNumber());
					bankMasterDTO.setContanctBankAddress(bankMaster.getContactBankAddress());
					bankMasterDTO.setGstNumber(bankMaster.getGstNumber());
					bankMasterDTO.setWebsite(bankMaster.getWebsite());
					bankMasterDTO.setStatus(bankMaster.getStatus());
					bankMasterDTO.setLogoPath(bankMaster.getBankLogoPath());
					bankMasterDTO.setEmail(bankMaster.getContactBankEmail());
					bankMasterDTO.setPhone(bankMaster.getContactBankPhone());
					bankMasterDTOs.add(bankMasterDTO);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", bankMasterDTOs);
				stopWatch.stop();
				logger.info("Get Bank Masters Success");
				logger.info(
						String.format("Time taken on Get Bank Masters Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Bank Masters Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.BankMasterNotFound);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", null);
				stopWatch.stop();
				logger.info("Get Bank Masters failed");
				logger.info(String.format("Bank Masters Not Found :-"));
				logger.info(
						String.format("Time taken on Get Bank Masters Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Bank Masters Service %s", CommonMessageLog.BankMasterNotFound));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Bank Masters Service.", e);
			logger.info(String.format("Time taken on Get Bank Masters Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Bank Masters Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> createCustomer(CreateCustomerRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Create Customer Master Service");
			CustomerMaster customerMasterExist = customerMasterRepository.findByCode(payload.getCustomerCode());
			if (customerMasterExist == null) {
				CustomerMaster customerMaster = new CustomerMaster();
				customerMaster.setCustomerName(payload.getCustomerName());
				customerMaster.setCode(payload.getCustomerCode());
				customerMaster.setState(payload.getState());
				customerMaster.setCity(payload.getCity());
				customerMaster.setContactPersonAddress(payload.getContactPersonAddress());
				customerMaster.setContactPersonEmail(payload.getContactPersonEmail());
				customerMaster.setContactPersonName(payload.getContactPersonName());
				customerMaster.setContactPersonPhone(payload.getContactPersonPhone());
				customerMaster.setStatus(PortalConstant.ACTIVE);
				customerMaster.setCreatedBy(payload.getUserId());
				customerMaster.setUpdatedBy(payload.getUserId());
				customerMaster.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				customerMaster.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));

				customerMasterRepository.save(customerMaster);
				auditService.saveAuditLogs(customerMaster);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Create Customer Master Success");
				logger.info(String.format("Time taken on Create Customer Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Create Customer Master Service %s", CommonMessageLog.SUCCESS));
				return responseMap;

			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CustomerCodeAlreadyExist);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Create Customer Master failed");
				logger.info(String.format("Customer Code Already Exist :-"));
				logger.info(String.format("Time taken on Create Customer Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Create Customer Master Service %s",
						CommonMessageLog.CustomerCodeAlreadyExist));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Create Customer Master Service.", e);
			logger.info(String.format("Time taken on Create Customer Master Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Customer Master Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateCustomer(UpdateCustomerRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Customer Master Service");
			CustomerMaster customerMasterExist = customerMasterRepository.findById(Long.parseLong(payload.getCustId()))
					.orElse(null);
			if (customerMasterExist != null) {
				customerMasterExist.setCustomerName(payload.getCustomerName());
				customerMasterExist.setState(payload.getState());
				customerMasterExist.setCity(payload.getCity());
				customerMasterExist.setContactPersonAddress(payload.getContactPersonAddress());
				customerMasterExist.setContactPersonEmail(payload.getContactPersonEmail());
				customerMasterExist.setContactPersonName(payload.getContactPersonName());
				customerMasterExist.setContactPersonPhone(payload.getContactPersonPhone());
				customerMasterExist.setUpdatedBy(payload.getUserId());
				customerMasterExist.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));

				customerMasterRepository.save(customerMasterExist);
				auditService.saveAuditLogs(customerMasterExist);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Customer Master Success");
				logger.info(String.format("Time taken on Update Customer Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Customer Master Service %s", CommonMessageLog.SUCCESS));
				return responseMap;

			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CustomerNotFound);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Customer Master failed");
				logger.info(String.format("Time taken on Update Customer Master Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Update Customer Master Service %s", CommonMessageLog.CustomerNotFound));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Customer Master Service.", e);
			logger.info(String.format("Time taken on Update Customer Master Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Customer Master Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getContracts(ContractDetailsRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of get Contracts Details Service");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<Contract> contractDetailsPage = null;
			String status = (payload.getStatus() != null && !payload.getStatus().isEmpty()) ? payload.getStatus()
					: PortalConstant.ACTIVE;
			if ((payload.getContractCode() != null && !payload.getContractCode().isEmpty())
					|| (payload.getCustomerName() != null && !payload.getCustomerName().isEmpty())
					|| (payload.getLenderName() != null && !payload.getLenderName().isEmpty())) {
				if (payload.getCustomerName() != null && !payload.getCustomerName().isEmpty()) {
					payload.setCustomerName("%" + payload.getCustomerName() + "%");
				}
				if (payload.getLenderName() != null && !payload.getLenderName().isEmpty()) {
					payload.setLenderName("%" + payload.getLenderName() + "%");
				}
				contractDetailsPage = contractRepository.getContractsDetailsUsingFilters(payload.getContractCode(),
						payload.getCustomerName(), payload.getLenderName(), pageRequest, status);
			} else {
				contractDetailsPage = contractRepository.getAllContractDetailsByStatus(status, pageRequest);
			}
			List<Contract> contractDetailsList = contractDetailsPage.getContent();
			if (contractDetailsList != null && !contractDetailsList.isEmpty()) {
				List<ContractDetailDTO> contractDetailDTOs = new ArrayList<ContractDetailDTO>();
				for (Contract contract : contractDetailsList) {
					ContractDetailDTO contractDetailDTO = new ContractDetailDTO();
					contractDetailDTO.setContractCode(contract.getCode());
					contractDetailDTO.setContractName(contract.getContractName());
					if (contract.getCustomerMaster() != null) {
						contractDetailDTO.setCustomerName(contract.getCustomerMaster().getCustomerName());
						contractDetailDTO.setCustId(contract.getCustomerMaster().getId().toString());
					}
					if (contract.getBankMaster() != null) {
						contractDetailDTO.setLenderName(contract.getBankMaster().getBankName());
					}
					contractDetailDTO.setContractStartDate(contract.getContractStartDate());
					contractDetailDTO.setContractEndDate(contract.getContractEndDate());
					contractDetailDTOs.add(contractDetailDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", contractDetailsPage.getTotalElements());
				responseMap.put("data", contractDetailDTOs);
				stopWatch.stop();
				logger.info("Get Contracts Details Success");
				logger.info(String.format("Time taken on get Contracts Details Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Contracts Details Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Contracts Details failed");
				logger.info(String.format("Contracts Records Not Found :- "));
				logger.info(String.format("Time taken on get Contracts Details Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Contracts Details Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Contracts Details Service.", e);
			logger.info(String.format("Time taken on get Contracts Details Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Contracts Details Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getCustomers(CustomerDetailsRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of get Customers Details Service");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<CustomerMaster> customersDetailsPage = null;
			if ((payload.getCustomerCode() != null && !payload.getCustomerCode().isEmpty())
					|| (payload.getCustomerName() != null && !payload.getCustomerName().isEmpty())) {
				if (payload.getCustomerName() != null && !payload.getCustomerName().isEmpty()) {
					payload.setCustomerName("%" + payload.getCustomerName() + "%");
				}
				customersDetailsPage = customerMasterRepository.getCustomersDetailsUsingFilters(
						payload.getCustomerCode(), payload.getCustomerName(), pageRequest, PortalConstant.ACTIVE);
			} else {
				customersDetailsPage = customerMasterRepository.getAllCustomersDetailsByStatus(PortalConstant.ACTIVE,
						pageRequest);
			}
			List<CustomerMaster> customerDetailsList = customersDetailsPage.getContent();
			if (customerDetailsList != null && !customerDetailsList.isEmpty()) {
				List<CustomerDetailsResponsePayload> customerDetailsResponsePayloads = new ArrayList<CustomerDetailsResponsePayload>();
				for (CustomerMaster customerMaster : customerDetailsList) {
					CustomerDetailsResponsePayload customerDetailsResponsePayload = new CustomerDetailsResponsePayload();
					customerDetailsResponsePayload.setId(customerMaster.getId().toString());
					customerDetailsResponsePayload.setCustomerName(customerMaster.getCustomerName());
					customerDetailsResponsePayload.setCustomerCode(customerMaster.getCode());
					customerDetailsResponsePayload.setCity(customerMaster.getCity());
					customerDetailsResponsePayload.setState(customerMaster.getState());
					customerDetailsResponsePayload.setStatus(customerMaster.getStatus());
					customerDetailsResponsePayload.setContactPersonName(customerMaster.getContactPersonName());
					customerDetailsResponsePayload.setContactPersonPhone(customerMaster.getContactPersonPhone());
					customerDetailsResponsePayload.setContactPersonEmail(customerMaster.getContactPersonEmail());
					customerDetailsResponsePayload.setContactPersonAddress(customerMaster.getContactPersonAddress());
					customerDetailsResponsePayloads.add(customerDetailsResponsePayload);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", customersDetailsPage.getTotalElements());
				responseMap.put("data", customerDetailsResponsePayloads);
				stopWatch.stop();
				logger.info("Get Customers Details Success");
				logger.info(String.format("Time taken on get Customers Details Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Customers Details Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Customers Details failed");
				logger.info(String.format("Customers Records Not Found :- "));
				logger.info(String.format("Time taken on get Customers Details Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Customers Details Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Customers Details Service.", e);
			logger.info(String.format("Time taken on get Customers Details Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Customers Details Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getContracts(ContractRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of get Contracts Details Service");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<Contract> contractDetailsList = contractRepository
					.getAllContractDetailsByStatus(PortalConstant.ACTIVE);
			if (contractDetailsList != null && !contractDetailsList.isEmpty()) {
				List<ContractDataDTO> contractDetailDTOs = new ArrayList<ContractDataDTO>();
				for (Contract contract : contractDetailsList) {
					ContractDataDTO contractDataDTO = new ContractDataDTO();
					contractDataDTO.setContractCode(contract.getCode());
					contractDataDTO.setContractName(contract.getContractName());
					if (contract.getCustomerMaster() != null) {
						contractDataDTO.setCustId(contract.getCustomerMaster().getId().toString());
					}
					contractDetailDTOs.add(contractDataDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", contractDetailDTOs);
				stopWatch.stop();
				logger.info("Get Contracts Details Success");
				logger.info(String.format("Time taken on get Contracts Details Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Contracts Details Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Contracts Details failed");
				logger.info(String.format("Contracts Records Not Found :- "));
				logger.info(String.format("Time taken on get Contracts Details Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Contracts Details Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Contracts Details Service.", e);
			logger.info(String.format("Time taken on get Contracts Details Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Contracts Details Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> addLenderEndPoints(LenderEndPointsRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Create Lender EndPoints Service");
			Contract contract = contractRepository.findByCode(payload.getContractCode());
			if (contract != null) {
				LenderEndPointsDTO lenderEndPointsDTO = payload.getData();
				LenderEndpointsConfig keyExist = lenderEndpointsConfigRepository
						.findByContrctCodeAndKey(payload.getContractCode(), lenderEndPointsDTO.getKey());
				if (keyExist != null) {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.KEYEXIST);
					responseMap.put("status", CommonMessageLog.FAILED);
					stopWatch.stop();
					logger.info("Create Lender EndPoints failed");
					logger.info(String.format("Key Already Exist :-"));
					logger.info(String.format("Time taken on Create Lender EndPoints Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Create Lender EndPoints Service %s", CommonMessageLog.KEYEXIST));
					return responseMap;
				}
				LenderEndpointsConfig lenderEndpointsConfig = new LenderEndpointsConfig();
				lenderEndpointsConfig.setContract(contract);
				lenderEndpointsConfig.setEndpoint(lenderEndPointsDTO.getLenderEndPoint());
				lenderEndpointsConfig.setKey(lenderEndPointsDTO.getKey());
				lenderEndpointsConfig.setStatus(PortalConstant.ACTIVE);
				lenderEndpointsConfig.setDeleted(false);
				lenderEndpointsConfig.setCreatedBy(payload.getUserId());
				lenderEndpointsConfig.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				lenderEndpointsConfig.setUpdatedBy(payload.getUserId());
				lenderEndpointsConfig.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));

				lenderEndpointsConfigRepository.save(lenderEndpointsConfig);
				auditService.saveAuditLogs(lenderEndpointsConfig);
				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Type", "application/json");
				String publishMessage = "";
				logger.info("Message to be published : " + publishMessage);
				cloudPubSubService.publishMessage(projectId, lenderEndPointTopicId, header, publishMessage);
				logger.info("Publish Success ? : " + true);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Create Lender EndPoints Success");
				logger.info(String.format("Time taken on Create Lender EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Create Lender EndPoints Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Create Lender EndPoints failed");
				logger.info(String.format("Time taken on Create Lender EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Create Lender EndPoints Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Create Lender EndPoints Service.", e);
			logger.info(String.format("Time taken on Create Lender EndPoints Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Lender EndPoints Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getLenderEndPoints(ContractDetailRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Get Lender EndPoints Service");
			Page<LenderEndpointsConfig> lenderEndPoints = lenderEndpointsConfigRepository
					.getLenderEndPointsByContractCode(payload.getContractCode(), pageRequest);
			if (lenderEndPoints != null && !lenderEndPoints.isEmpty()) {
				List<LenderEndPointsDTO> lenderEndpointDTOs = new ArrayList<LenderEndPointsDTO>();
				for (LenderEndpointsConfig lenderEndpointsConfig : lenderEndPoints) {
					LenderEndPointsDTO lenderEndPointsDTO = new LenderEndPointsDTO();
					lenderEndPointsDTO.setDeleted(lenderEndpointsConfig.isDeleted() ? "true" : "false");
					lenderEndPointsDTO.setId(lenderEndpointsConfig.getId().toString());
					lenderEndPointsDTO.setKey(lenderEndpointsConfig.getKey());
					lenderEndPointsDTO.setLenderEndPoint(lenderEndpointsConfig.getEndpoint());
					lenderEndPointsDTO.setStatus(lenderEndpointsConfig.getStatus());
					lenderEndpointDTOs.add(lenderEndPointsDTO);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", lenderEndpointDTOs);
				responseMap.put("count", lenderEndPoints.getTotalElements());
				stopWatch.stop();
				logger.info("Get Lender EndPoints Success");
				logger.info(String.format("Time taken on Get Lender EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Lender EndPoints Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Lender EndPoints failed");
				logger.info(String.format("Time taken on Get Lender EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Lender EndPoints Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Lender EndPoints Service.", e);
			logger.info(
					String.format("Time taken on Get Lender EndPoints Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Lender EndPoints Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateLenderEndPoints(LenderEndPointsDTO payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Lender EndPoints Service");
			LenderEndpointsConfig lenderEndpointExist = lenderEndpointsConfigRepository
					.findById(Long.parseLong(payload.getId())).orElse(null);
			if (lenderEndpointExist != null) {
				lenderEndpointExist.setEndpoint(payload.getLenderEndPoint());
				lenderEndpointExist.setStatus(payload.getStatus());
				lenderEndpointExist.setDeleted(Boolean.parseBoolean(payload.getDeleted()));
				lenderEndpointExist.setUpdatedBy(payload.getUserId());
				lenderEndpointExist.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				lenderEndpointsConfigRepository.save(lenderEndpointExist);
				auditService.saveAuditLogs(lenderEndpointExist);
				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Type", "application/json");
				String publishMessage = "";
				logger.info("Message to be published : " + publishMessage);
				cloudPubSubService.publishMessage(projectId, lenderEndPointTopicId, header, publishMessage);
				logger.info("Publish Success ? : " + true);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Lender EndPoints Success");
				logger.info(String.format("Time taken on Update Lender EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Lender EndPoints Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Lender EndPoints failed");
				logger.info(String.format("Time taken on Update Lender EndPoints Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Update Lender EndPoints Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Lender EndPoints Service.", e);
			logger.info(String.format("Time taken on Update Lender EndPoints Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Lender EndPoints Service.");
			return responseMap;
		}
	}
}
