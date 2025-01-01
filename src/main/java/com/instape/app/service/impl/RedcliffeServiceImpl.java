package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.instape.app.cloudsql.model.BloodBookingSummaryDTO;
import com.instape.app.cloudsql.model.Contract;
import com.instape.app.cloudsql.model.ContractOffers;
import com.instape.app.cloudsql.model.EmployeeInfo;
import com.instape.app.cloudsql.model.LoanRecord;
import com.instape.app.cloudsql.model.OffersAvailMapping;
import com.instape.app.cloudsql.model.WorkflowRun;
import com.instape.app.cloudsql.model.WorkflowStep;
import com.instape.app.cloudsql.repository.ContractOffersRepository;
import com.instape.app.cloudsql.repository.ContractRepository;
import com.instape.app.cloudsql.repository.EmployeeInfoRepository;
import com.instape.app.cloudsql.repository.OffersAvailMappingRepository;
import com.instape.app.cloudsql.repository.WorkflowRunRepository;
import com.instape.app.cloudsql.repository.WorkflowStepRepository;
import com.instape.app.request.BookBloodTestRequestPayload;
import com.instape.app.request.BookingSummaryRequestPayload;
import com.instape.app.request.RedcliffeBookingPayload;
import com.instape.app.response.RedCliffeBookingSuccessResponse;
import com.instape.app.response.RedcliffeBookingBadResponse;
import com.instape.app.service.RedcliffeService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.HashUtils;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 09-Jul-2024
 * @ModifyDate - 09-Jul-2024
 * @Desc -
 */
@Service
public class RedcliffeServiceImpl implements RedcliffeService {

	static final Logger logger = LogManager.getFormatterLogger(RedcliffeServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${BOOK_BLOOD_TEST_URL}")
	private String bookBloodTestURL;

	@Value("${KEY}")
	private String key;

	@Autowired
	private EmployeeInfoRepository employeeInfoRepository;

	@Autowired
	private ContractOffersRepository contractOffersRepository;

	@Autowired
	private OffersAvailMappingRepository offersAvailMappingRepository;

	@Autowired
	private WorkflowStepRepository workflowStepRepository;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private WorkflowRunRepository workflowRunRepository;

	@Autowired
	private HashUtils hashUtils;

	@Autowired
	private EntityManager entityManager;

	@Override
	public Map<Object, Object> bookBlodTest(BookBloodTestRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Book Blood Test service %s, %s, %s, %s, %s", payload.getCustId(),
				payload.getEmpId(), payload.getUserId(), payload.getContractCode(), payload.getOfferCode()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {

			EmployeeInfo empInfo = employeeInfoRepository.getEmployeeByEmployeeIdAndStatus(payload.getEmpId(),
					Long.parseLong(payload.getCustId()), PortalConstant.INACTIVE);

			Contract contract = contractRepository.findByCode(payload.getContractCode());

			if (empInfo != null) {

				int code = PortalConstant.OK;
				String message = CommonMessageLog.SUCCESS;
				String status = CommonMessageLog.SUCCESS;

				logger.info("Employee Record Exist...");

				ContractOffers contractOfferExist = contractOffersRepository.getContractOfferByContractCodeAndOfferCode(
						payload.getContractCode(), payload.getOfferCode(), true);

				if (contractOfferExist != null) {

					logger.info("Contract Offer Mapping Record Exist...");

					OffersAvailMapping offerAvailed = offersAvailMappingRepository.getEmployeeAvailedOfferRecord(
							payload.getContractCode(), payload.getOfferCode(), empInfo.getCode());
					if (offerAvailed == null) {
						logger.info("Employee Offer Availed Records Not Found");
						logger.info("Employee is Eligible for Offer...");

						RedcliffeBookingPayload requestPayload = new RedcliffeBookingPayload();

						requestPayload.setCustomerEmail(empInfo.getEmail());
						logger.info("Employee Email Set to Payload Success");

						requestPayload.setCustomerName(empInfo.getEmployeeName());
						logger.info("Employee Name Set to Payload Success");

						String gender = empInfo.getGender();
						String designation = null;

						if (gender != null && !gender.isEmpty()) {
							designation = PortalConstant.M.equalsIgnoreCase(gender.substring(0, 1)) ? PortalConstant.MR
									: PortalConstant.MRS;

							requestPayload.setCustomerGender(gender.toLowerCase());
							logger.info("Employee Gender Set to Payload Success");

							requestPayload.setDesignation(designation);
							logger.info("Employee Designation Set to Payload Success");
						}

						requestPayload.setCustomerAge(Integer.parseInt(payload.getAge()));
						logger.info("Employee Age Set to Payload Success");

						requestPayload.setCustomerMobile(payload.getMobile());
						logger.info("Employee Mobile Set to Payload Success");

						requestPayload.setCampCode(contractOfferExist.getCampCode());
						logger.info("Employee Camp Code Set to Payload Success");

						UUID uuid = UUID.randomUUID();
						String referenceId = uuid.toString();
						requestPayload.setReferenceData(referenceId);
						logger.info("Employee Reference Data Set to Payload Success");

						if (contractOfferExist.getPackageValue() != null) {
							ObjectMapper objectMapper = new ObjectMapper();
							Map<String, String> resultMap = new HashMap<>();

							// Parse JSON array
							JsonNode rootArray = objectMapper.readTree(contractOfferExist.getPackageValue().toString());

							// Iterate over each JSON object in the array
							for (JsonNode objNode : rootArray) {
								Iterator<Map.Entry<String, JsonNode>> fields = objNode.fields();
								while (fields.hasNext()) {
									Map.Entry<String, JsonNode> field = fields.next();
									resultMap.put(field.getKey(), field.getValue().asText());
								}
							}
							String packages = resultMap.get(PortalConstant.PACKAGES);
							if (packages != null && !packages.isEmpty()) {
								String[] splitPackageValues = packages.split(",");
								ArrayList<String> packageValues = new ArrayList<String>();
								for (String value : splitPackageValues) {
									String trimmedValue = value.trim();
									packageValues.add(new String(trimmedValue));
								}
								requestPayload.setPackages(packageValues);
								logger.info("Employee Packages Set to Payload Success");
							}
						}

						String functionName = PortalConstant.FunctionName_BookBloodTest;
						String workflowName = PortalConstant.WorkflowName_RedcliffWorkflow;
						WorkflowStep workflowStep = workflowStepRepository.getWorkflowStepByContractId(contract.getId(),
								functionName, workflowName);

						logger.info(requestPayload);
						WorkflowRun workflowRun = new WorkflowRun();
						workflowRun.setCreatedBy(payload.getUserId());
						workflowRun.setUpdatedBy(payload.getUserId());
						workflowRun.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
						workflowRun.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
						workflowRun.setFunctionName(functionName);
						workflowRun.setRequestPayload(StringUtil.pojoToString(requestPayload));
						workflowRun.setContract(contract);
						workflowRun.setEmployeeInfo(empInfo);
						workflowRun.setWorkflowStep(workflowStep);
						workflowRun.setRecordStatus(PortalConstant.VALID);

						logger.info("Start of Calling Redcliffe Book Blood Test API request Process...");

						HttpHeaders headers = new HttpHeaders();
						headers.set("Content-Type", "application/json");
						headers.set("key", key);
						logger.info("Set Headers Success");

						HttpEntity<RedcliffeBookingPayload> entity = new HttpEntity<>(requestPayload, headers);
						logger.info("Redcliffe Book Blood Test URL : " + bookBloodTestURL);

						try {
							RedCliffeBookingSuccessResponse response = restTemplate.exchange(bookBloodTestURL,
									HttpMethod.POST, entity, RedCliffeBookingSuccessResponse.class).getBody();

							logger.info("Redcliffe Blood Test Booking Success Response Payload : " + response);
							workflowRun.setResponsePayload(StringUtil.pojoToString(response));
							workflowRun.setStatus(PortalConstant.SUCCESS);

							OffersAvailMapping availMapping = new OffersAvailMapping();
							availMapping.setContractOffers(contractOfferExist);
							availMapping.setEmployeeInfo(empInfo);
							availMapping.setAvailedAt(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							availMapping.setCreatedBy(payload.getUserId());
							availMapping.setUpdatedBy(payload.getUserId());
							availMapping.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							availMapping.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							availMapping.setStatus(PortalConstant.AVAILED);
							availMapping.setOfferBookingId(response.getBookingId());
							availMapping.setRefId(referenceId);
							offersAvailMappingRepository.save(availMapping);

							responseMap.put("bookingId", response.getBookingId());

						} catch (HttpClientErrorException e) {
							RedcliffeBookingBadResponse badResponse = e
									.getResponseBodyAs(RedcliffeBookingBadResponse.class);
							logger.info("Redcliffe Blood Test Booking Bad Response Payload : " + badResponse);
							workflowRun.setResponsePayload(StringUtil.pojoToString(badResponse));
							workflowRun.setStatus(PortalConstant.FAILED);

							code = PortalConstant.OK;
							message = badResponse.getMessage();
							status = CommonMessageLog.FAILED;

						} catch (Exception e) {
							logger.info("Redcliffe Blood Test Booking Internal Server Error Response Payload : "
									+ e.getMessage());
							RedcliffeBookingBadResponse internalServerError = new RedcliffeBookingBadResponse();
							internalServerError.setStatus(PortalConstant.ERROR);
							internalServerError.setMessage(e.getMessage());

							workflowRun.setResponsePayload(StringUtil.pojoToString(internalServerError));
							workflowRun.setStatus(PortalConstant.ERROR);

							code = PortalConstant.INTERNALSERVERERROR;
							message = CommonMessageLog.SomeThingWentWrong;
							status = CommonMessageLog.FAILED;
						}

						if (workflowStep != null && contract != null && empInfo != null) {
							workflowRunRepository.save(workflowRun);
							logger.info("Workflow Run Record Saved Successfully...");
						}

						responseMap.put("code", code);
						responseMap.put("message", message);
						responseMap.put("status", status);
						stopWatch.stop();
						logger.info("Book Blood Test Status : " + message);
						logger.info(String.format("Time taken on Book Blood Test Service :- %s",
								stopWatch.getTotalTimeSeconds()));
						logger.info(String.format("End of Book Blood Test Service %s", message));
						return responseMap;
					} else {
						logger.info("Employee Offer Availed Records Found");
						logger.info("Employee is Not Eligible for Offer...");
						responseMap.put("code", PortalConstant.OK);
						responseMap.put("message", CommonMessageLog.OFFERALREADYAVAILED);
						responseMap.put("status", CommonMessageLog.FAILED);
						stopWatch.stop();
						logger.info("Book Blood Test failed");
						logger.info(String.format("Employee Already Availed Offer :- %s, %s", payload.getCustId(),
								payload.getEmpId()));
						logger.info(String.format("Time taken on Book Blood Test Service :- %s",
								stopWatch.getTotalTimeSeconds()));
						logger.info(String.format("End of Book Blood Test Service %s",
								CommonMessageLog.CONTRACTOFFERNOTFOUND));
						responseMap.put("status", CommonMessageLog.FAILED);
						return responseMap;
					}
				} else {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.CONTRACTOFFERNOTFOUND);
					responseMap.put("status", CommonMessageLog.FAILED);
					stopWatch.stop();
					logger.info("Book Blood Test failed");
					logger.info(String.format("Contract Offer Not Found :- %s, %s", payload.getCustId(),
							payload.getEmpId()));
					logger.info(String.format("Time taken on Book Blood Test Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(
							String.format("End of Book Blood Test Service %s", CommonMessageLog.CONTRACTOFFERNOTFOUND));
					return responseMap;
				}
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.EMPNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Book Blood Test failed");
				logger.info(String.format("Employee Not Found :- %s, %s", payload.getCustId(), payload.getEmpId()));
				logger.info(
						String.format("Time taken on Book Blood Test Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Book Blood Test Service %s", CommonMessageLog.EMPNOTFOUND));
				return responseMap;
			}

		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Book Blood Test Service.", e);
			logger.info(String.format("Time taken on Book Blood Test Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Book Blood Test Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getBookingSummary(BookingSummaryRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of get Blood Booking Summary Service");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Map<String, Object> bookingRecordsMap = getBookingRecords(payload, pageRequest);
			List<OffersAvailMapping> bookingRecords = (List<OffersAvailMapping>) bookingRecordsMap.get("data");
			Long bookingRecordsCount = (Long) bookingRecordsMap.get("toatlCount");
			List<OffersAvailMapping> downloadBookingRecords = (List<OffersAvailMapping>) bookingRecordsMap
					.get("downloadData");
			if (bookingRecords != null && !bookingRecords.isEmpty() && downloadBookingRecords != null
					&& !downloadBookingRecords.isEmpty()) {
				ArrayList<BloodBookingSummaryDTO> bloodBookingSummaryDTOs = new ArrayList<BloodBookingSummaryDTO>();
				for (OffersAvailMapping bookingRecord : bookingRecords) {
					BloodBookingSummaryDTO bloodBookingSummaryDTO = new BloodBookingSummaryDTO();
					bloodBookingSummaryDTO.setBookingId(bookingRecord.getOfferBookingId());
					bloodBookingSummaryDTO.setOfferCode(bookingRecord.getContractOffers().getOffers().getOfferCode());
					bloodBookingSummaryDTO.setEmpId(bookingRecord.getEmployeeInfo().getEmpId());
					bloodBookingSummaryDTO.setEmpName(bookingRecord.getEmployeeInfo().getEmployeeName());
					bloodBookingSummaryDTO.setBookingDate(bookingRecord.getCreatedDate());
					bloodBookingSummaryDTO.setStatus(bookingRecord.getStatus());
					bloodBookingSummaryDTO.setReferenceId(bookingRecord.getRefId());
					bloodBookingSummaryDTOs.add(bloodBookingSummaryDTO);
				}
				ArrayList<BloodBookingSummaryDTO> downloadSummaryDTOs = new ArrayList<BloodBookingSummaryDTO>();
				for (OffersAvailMapping bookingRecord : downloadBookingRecords) {
					BloodBookingSummaryDTO bloodBookingSummaryDTO = new BloodBookingSummaryDTO();
					bloodBookingSummaryDTO.setBookingId(bookingRecord.getOfferBookingId());
					bloodBookingSummaryDTO.setOfferCode(bookingRecord.getContractOffers().getOffers().getOfferCode());
					bloodBookingSummaryDTO.setEmpId(bookingRecord.getEmployeeInfo().getEmpId());
					bloodBookingSummaryDTO.setEmpName(bookingRecord.getEmployeeInfo().getEmployeeName());
					bloodBookingSummaryDTO.setBookingDate(bookingRecord.getCreatedDate());
					bloodBookingSummaryDTO.setStatus(bookingRecord.getStatus());
					bloodBookingSummaryDTO.setReferenceId(bookingRecord.getRefId());
					downloadSummaryDTOs.add(bloodBookingSummaryDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", bookingRecordsCount);
				responseMap.put("data", bloodBookingSummaryDTOs);
				responseMap.put("downloadData", downloadSummaryDTOs);
				stopWatch.stop();
				logger.info("Get Blood Booking Summary Success");
				logger.info(String.format("Time taken on get Blood Booking Summary Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Blood Booking Summary Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", new ArrayList());
				responseMap.put("downloadData", new ArrayList());
				stopWatch.stop();
				logger.info("Get Blood Booking Summary failed");
				logger.info("Blood Booking Summary Records Not Found :- ");
				logger.info(String.format("Time taken on get Blood Booking Summary Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of get Blood Booking Summary Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Blood Booking Summary Service.", e);
			logger.info(String.format("Time taken on get Blood Booking Summary Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Blood Booking Summary Service.");
			return responseMap;
		}
	}

	public Map<String, Object> getBookingRecords(BookingSummaryRequestPayload payload, PageRequest pageRequest) {
		String dynamicQuery = "SELECT o from OffersAvailMapping o where o.id > 0";
		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND o.createdDate >= :startDate AND o.createdDate <= :endDate";
		}
		if (payload.getEmpId() != null && !payload.getEmpId().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND o.employeeInfo.empId = :empId";
		}
		if (payload.getCustId() != null && !payload.getCustId().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND o.employeeInfo.customerMaster.id = :custId";
		}
		if (payload.getMobile() != null && !payload.getMobile().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND o.employeeInfo.mobSHA3 = :mobile";
		}
		if (payload.getStatus() != null && !payload.getStatus().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND o.status ilike:status";
		}
		dynamicQuery = dynamicQuery + " order by o.createdDate desc";
		Query query = entityManager.createQuery(dynamicQuery, OffersAvailMapping.class);

		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		query.setFirstResult((pageNumber) * pageSize);
		query.setMaxResults(pageSize);

		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			query.setParameter("startDate", Timestamp.valueOf(payload.getStartDate()));
			query.setParameter("endDate", Timestamp.valueOf(payload.getEndDate()));
		}
		if (payload.getEmpId() != null && !payload.getEmpId().isEmpty()) {
			query.setParameter("empId", payload.getEmpId());
		}
		if (payload.getCustId() != null && !payload.getCustId().isEmpty()) {
			query.setParameter("custId", payload.getCustId());
		}
		if (payload.getMobile() != null && !payload.getMobile().isEmpty()) {
			query.setParameter("mobile", hashUtils.getSHA3(payload.getMobile()));
		}
		if (payload.getStatus() != null && !payload.getStatus().isEmpty()) {
			query.setParameter("status", payload.getStatus());
		}

		String dynamicQueryDownload = "SELECT o from OffersAvailMapping o where o.id > 0";
		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload
					+ " AND o.createdDate >= :startDate AND o.createdDate <= :endDate";
		}
		if (payload.getEmpId() != null && !payload.getEmpId().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND o.employeeInfo.empId = :empId";
		}
		if (payload.getCustId() != null && !payload.getCustId().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND o.employeeInfo.customerMaster.id = :custId";
		}
		if (payload.getMobile() != null && !payload.getMobile().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND o.employeeInfo.mobSHA3 = :mobile";
		}
		if (payload.getStatus() != null && !payload.getStatus().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND o.status ilike:status";
		}
		dynamicQueryDownload = dynamicQueryDownload + " order by o.createdDate desc";
		Query queryDownload = entityManager.createQuery(dynamicQueryDownload, OffersAvailMapping.class);

		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			queryDownload.setParameter("startDate", Timestamp.valueOf(payload.getStartDate()));
			queryDownload.setParameter("endDate", Timestamp.valueOf(payload.getEndDate()));
		}
		if (payload.getEmpId() != null && !payload.getEmpId().isEmpty()) {
			queryDownload.setParameter("empId", payload.getEmpId());
		}
		if (payload.getCustId() != null && !payload.getCustId().isEmpty()) {
			queryDownload.setParameter("custId", payload.getCustId());
		}
		if (payload.getMobile() != null && !payload.getMobile().isEmpty()) {
			queryDownload.setParameter("mobile", hashUtils.getSHA3(payload.getMobile()));
		}
		if (payload.getStatus() != null && !payload.getStatus().isEmpty()) {
			queryDownload.setParameter("status", payload.getStatus());
		}
		List<LoanRecord> bookingRecords = query.getResultList();
		List<LoanRecord> downloadRecords = queryDownload.getResultList();
		Long bookingRecordsCount = (long) downloadRecords.size();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult.put("data", bookingRecords);
		mapResult.put("toatlCount", bookingRecordsCount);
		mapResult.put("downloadData", downloadRecords);
		return mapResult;
	}
}