package com.instape.app.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.instape.app.cloudsql.model.Contract;
import com.instape.app.cloudsql.model.CustomerMaster;
import com.instape.app.cloudsql.model.EmployeeDataDTO;
import com.instape.app.cloudsql.model.EmployeeInfo;
import com.instape.app.cloudsql.model.EmployeeInfoDTO;
import com.instape.app.cloudsql.model.EmployeeSearchDTO;
import com.instape.app.cloudsql.model.ExcelUpload;
import com.instape.app.cloudsql.model.LenderRecord;
import com.instape.app.cloudsql.model.OffersAvailMapping;
import com.instape.app.cloudsql.repository.ContractRepository;
import com.instape.app.cloudsql.repository.EmployeeInfoRepository;
import com.instape.app.cloudsql.repository.ExcelUploadRepository;
import com.instape.app.cloudsql.repository.LenderRecordRepository;
import com.instape.app.cloudsql.repository.OffersAvailMappingRepository;
import com.instape.app.cloudstore.service.CloudStorageService;
import com.instape.app.cloudstore.service.EncryptionDecryptionService;
import com.instape.app.request.EmployeeDetailsRequestPayload;
import com.instape.app.request.EmployeeFetchRequestPayload;
import com.instape.app.request.EmployeeSearchByMobile;
import com.instape.app.request.EmployeeUploadRequestPayload;
import com.instape.app.service.EmployeeDetailsService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.EncryptionHelper;
import com.instape.app.utils.FirebaseScreen;
import com.instape.app.utils.HashUtils;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 03-Jan-2024
 * @ModifyDate - 03-Jan-2024
 * @Desc -
 */
@Service
public class EmployeeDetaisServiceImpl implements EmployeeDetailsService {

	static final Logger logger = LogManager.getFormatterLogger(EmployeeDetaisServiceImpl.class);

	@Autowired
	private EmployeeInfoRepository employeeInfoRepository;

	@Autowired
	private HashUtils hashUtils;

	@Autowired
	private LenderRecordRepository lenderRecordRepository;

	@Autowired
	private OffersAvailMappingRepository offersAvailMappingRepository;

	@Autowired
	private CloudStorageService cloudStorageService;

	@Value("${BUCKET_NAME}")
	private String EMPLOYEE_PROFILE_PIC_BUCKET;

	@Autowired
	private ExcelUploadRepository excelUploadRepository;

	@Autowired
	private ContractRepository contractRepository;

	@Value("${EMPLOYEE_UPLOAD_FILE_BUCKET}")
	private String EMPLOYEE_UPLOAD_FILE_BUCKET;
	
	@Autowired
	private EncryptionDecryptionService encryptionDecryptionService;

	@Override
	public Map<Object, Object> searchEmployeeByMobileNumber(EmployeeSearchByMobile payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new HashMap<Object, Object>();
		logger.info(String.format("Start of Search Employee By Mobile Number Service %s, %s, %s", payload.getCustId(),
				payload.getCustCode(), payload.getMobileNumber()));
		try {
			EmployeeInfo employee = employeeInfoRepository.getEmployeeInfoByMobileNumberAndContractCode(
					payload.getCustCode(), hashUtils.getSHA3(payload.getMobileNumber()), PortalConstant.INACTIVE);
			if (employee != null) {
				List<LenderRecord> lenderRecords = lenderRecordRepository.getLenderRecordsByCustomerCodeAndEmployeeCode(
						payload.getCustCode(), employee.getCode(), PortalConstant.VALID);
				if (lenderRecords != null && !lenderRecords.isEmpty()) {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.SUCCESS);
					responseMap.put("status", CommonMessageLog.SUCCESS);
					EmployeeSearchDTO loanApproverInfo = new EmployeeSearchDTO(employee.getCode(),
							employee.getEmployeeName(), PortalConstant.ACTIVE);
					List<EmployeeSearchDTO> loanApproverList = new ArrayList<EmployeeSearchDTO>();
					loanApproverList.add(loanApproverInfo);
					responseMap.put("data", loanApproverList);
					stopWatch.stop();
					logger.info("Search Employee By Mobile Number Success");
					logger.info(String.format("Time taken on Search Employee By Mobile Number Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Search Employee By Mobile Number Service %s",
							CommonMessageLog.SUCCESS));
					return responseMap;
				} else {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.EmployeeNotOnborded);
					responseMap.put("status", CommonMessageLog.FAILED);
					responseMap.put("data", new ArrayList<>());
					stopWatch.stop();
					logger.info("Search Employee By Mobile Number Failed");
					logger.info(String.format("Time taken on Search Employee By Mobile Number Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Search Employee By Mobile Number Service %s",
							CommonMessageLog.FAILED));
					return responseMap;
				}
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.EMPNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList<>());
				stopWatch.stop();
				logger.info("Search Employee By Mobile Number Failed");
				logger.info(String.format("Time taken on Search Employee By Mobile Number Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Search Employee By Mobile Number Service %s",
						CommonMessageLog.EMPNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			responseMap.put("data", new ArrayList<>());
			stopWatch.stop();
			logger.info("Search Employee By Mobile Number Failed");
			logger.info(String.format("Time taken on Search Employee By Mobile Number Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Search Employee By Mobile Number Service %s",
					PortalConstant.INTERNALSERVERERROR));
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> searchEmployeeByCustomerId(EmployeeFetchRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of get Employee Details Service %s, %s", payload.getCustId(),
				payload.getContractCode()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			EmployeeInfo employeeInfo = employeeInfoRepository.getEmployeeInfoByMobileNumberAndContractCode(
					payload.getContractCode(), hashUtils.getSHA3(payload.getMobile()), PortalConstant.INACTIVE);

			if (employeeInfo != null) {
				logger.info("Employee Details Found");
				EmployeeDataDTO employeeDataDTO = new EmployeeDataDTO();
				employeeDataDTO.setDesignation(employeeInfo.getDesignation());
				employeeDataDTO.setEmpId(employeeInfo.getEmpId());
				employeeDataDTO.setEmployeeName(employeeInfo.getEmployeeName());
				employeeDataDTO.setGender(employeeInfo.getGender());
				employeeDataDTO.setStatus(employeeInfo.getStatus());
				employeeDataDTO.setJourneyStatus(employeeInfo.getJourneyStatus());
				String onboardingStatus = PortalConstant.NO;
				List<LenderRecord> lenderRecordsExist = lenderRecordRepository
						.getLenderRecordsByCustomerCodeAndEmployeeCode(payload.getContractCode(),
								employeeInfo.getCode(), PortalConstant.VALID);
				if (lenderRecordsExist != null && !lenderRecordsExist.isEmpty()) {
					logger.info("Employee Lender Records Found");
					onboardingStatus = PortalConstant.YES;
					employeeDataDTO.setOnboradingDate(lenderRecordsExist.get(0).getCreatedDate());
				}
				employeeDataDTO.setOnboardingStatus(onboardingStatus);
				String empImage = null;
				logger.info("Getting Image from Employee Profile Pic Bucket");
				empImage = getEmployeeImageFromBucket(payload.getContractCode(), employeeInfo.getCode());
				employeeDataDTO.setImage(empImage);

				OffersAvailMapping offerAvailed = offersAvailMappingRepository.getEmployeeAvailedOfferRecord(
						payload.getContractCode(), payload.getOfferCode(), employeeInfo.getCode());
				if (offerAvailed != null) {
					logger.info("Employee Offer Availed Records Found");
					employeeDataDTO.setEligibleForOffer(false);
				} else {
					logger.info("Employee Offer Availed Records Not Found");
					employeeDataDTO.setEligibleForOffer(true);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", employeeDataDTO);
				stopWatch.stop();
				logger.info("get Employee Details Success");
				logger.info(String.format("Time taken on get Employee Details Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Employee Details Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("get Employee Details failed");
				logger.info(String.format("Employee Records Not Found :- %s", payload.getCustId()));
				logger.info(String.format("Time taken on get Employee Details Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Employee Details Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Employee Details Service.", e);
			logger.info(
					String.format("Time taken on get Employee Details Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Employee Details Service.");
			return responseMap;
		}
	}

	public String getEmployeeImageFromBucket(String customerCode, String employeeCode) throws IOException {
		String profilePicPath = FirebaseScreen.PROFILEPICS + "/" + customerCode + "/" + employeeCode + "/"
				+ FirebaseScreen.THUMBPNG;
		return cloudStorageService.getImageFromBucket(EMPLOYEE_PROFILE_PIC_BUCKET, profilePicPath);
	}

	@Override
	public Map<Object, Object> getEmployees(EmployeeDetailsRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of get Employee Details Service %s", payload.getContractCode()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<EmployeeInfo> empDetailsPage = null;
			if ((payload.getEmployeeId() != null && !payload.getEmployeeId().isEmpty())
					|| (payload.getEmployeeMobile() != null && !payload.getEmployeeMobile().isEmpty())
					|| (payload.getEmployeeName() != null && !payload.getEmployeeName().isEmpty())
					|| (payload.getDesignation() != null && !payload.getDesignation().isEmpty())
					|| (payload.getCity() != null && !payload.getCity().isEmpty())
					|| (payload.getState() != null && !payload.getState().isEmpty())
					|| (payload.getStatus() != null && !payload.getStatus().isEmpty())
					|| (payload.getGender() != null && !payload.getGender().isEmpty())
					|| (payload.getJourneyStatus() != null && !payload.getJourneyStatus().isEmpty())) {
				if (payload.getEmployeeName() != null && !payload.getEmployeeName().isEmpty()) {
					payload.setEmployeeName("%" + payload.getEmployeeName() + "%");
				}
				if (payload.getEmployeeMobile() != null && !payload.getEmployeeMobile().isEmpty()) {
					payload.setEmployeeMobile(hashUtils.getSHA3(payload.getEmployeeMobile()));
				}
				if (payload.getDesignation() != null && !payload.getDesignation().isEmpty()) {
					payload.setDesignation("%" + payload.getDesignation() + "%");
				}
				if (payload.getGender() != null && !payload.getGender().isEmpty()) {
					payload.setGender("%" + payload.getGender() + "%");
				}
				if (payload.getState() != null && !payload.getState().isEmpty()) {
					payload.setState("%" + payload.getState() + "%");
				}
				if (payload.getCity() != null && !payload.getCity().isEmpty()) {
					payload.setCity("%" + payload.getCity() + "%");
				}
				if (payload.getStatus() != null && !payload.getStatus().isEmpty()) {
					payload.setStatus("%" + payload.getStatus() + "%");
				}
				if (payload.getJourneyStatus() != null && !payload.getJourneyStatus().isEmpty()) {
					payload.setJourneyStatus("%" + payload.getJourneyStatus() + "%");
				}
				empDetailsPage = employeeInfoRepository.getEmployeeDetailsUsingFilters(payload.getContractCode(),
						payload.getEmployeeId(), payload.getEmployeeName(), payload.getEmployeeMobile(),
						payload.getDesignation(), payload.getCity(), payload.getState(), payload.getGender(),
						payload.getStatus(), payload.getJourneyStatus(), pageRequest, PortalConstant.INACTIVE);
			} else if (payload.getFcmSet() != null && !payload.getFcmSet().isEmpty()) {

				if (payload.getFcmSet().equalsIgnoreCase(PortalConstant.YES)) {
					if (payload.getCifSet() != null && !payload.getCifSet().isEmpty()) {

						if (payload.getCifSet().equalsIgnoreCase(PortalConstant.YES)) {
							empDetailsPage = employeeInfoRepository
									.getEmployeeDetailsByCustomerIdAndFCMNotNullAndOnboarded(payload.getContractCode(),
											PortalConstant.INACTIVE, pageRequest);
						} else {
							empDetailsPage = employeeInfoRepository
									.getEmployeeDetailsByCustomerIdAndFCMNotNullAndNotOnboarded(
											payload.getContractCode(), PortalConstant.INACTIVE, pageRequest);
						}
					} else {
						empDetailsPage = employeeInfoRepository.getEmployeeDetailsByCustomerIdAndFCMNotNull(
								payload.getContractCode(), PortalConstant.INACTIVE, pageRequest);
					}
				} else {
					if (payload.getCifSet() != null && !payload.getCifSet().isEmpty()) {

						if (payload.getCifSet().equalsIgnoreCase(PortalConstant.YES)) {
							empDetailsPage = employeeInfoRepository
									.getEmployeeDetailsByCustomerIdAndFCMNullAndOnboarded(payload.getContractCode(),
											PortalConstant.INACTIVE, pageRequest);
						} else {
							empDetailsPage = employeeInfoRepository
									.getEmployeeDetailsByCustomerIdAndFCMNullAndNotOnboarded(payload.getContractCode(),
											PortalConstant.INACTIVE, pageRequest);
						}
					} else {
						empDetailsPage = employeeInfoRepository.getEmployeeDetailsByCustomerIdAndFCMNull(
								payload.getContractCode(), PortalConstant.INACTIVE, pageRequest);
					}
				}
			} else if (payload.getCifSet() != null && !payload.getCifSet().isEmpty()) {
				if (payload.getCifSet().equalsIgnoreCase(PortalConstant.YES)) {
					empDetailsPage = employeeInfoRepository.getEmployeeDetailsByCustomerIdAndOnboarded(
							payload.getContractCode(), PortalConstant.INACTIVE, pageRequest);
				} else {
					empDetailsPage = employeeInfoRepository.getEmployeeDetailsByCustomerIdAndNotOnboarded(
							payload.getContractCode(), PortalConstant.INACTIVE, pageRequest);
				}
			} else {
				empDetailsPage = employeeInfoRepository.getEmployeeDetailsByCustomerId(payload.getContractCode(),
						PortalConstant.INACTIVE, pageRequest);
			}
			List<EmployeeInfo> empDetailsList = empDetailsPage.getContent();
			if (empDetailsList != null && !empDetailsList.isEmpty()) {

				List<EmployeeInfoDTO> empDetailsDTOs = new ArrayList<EmployeeInfoDTO>();
				for (EmployeeInfo employeeInfo : empDetailsList) {
					EmployeeInfoDTO empDTO = new EmployeeInfoDTO();
					empDTO.setCode(employeeInfo.getCode());
					empDTO.setDateOfLeaving(employeeInfo.getDateOfLeaving());
					empDTO.setDesignation(employeeInfo.getDesignation());
					empDTO.setEmpId(employeeInfo.getEmpId());
					empDTO.setGender(employeeInfo.getGender());
					empDTO.setJoiningDate(employeeInfo.getJoiningDate());
					empDTO.setEmployeeName(employeeInfo.getEmployeeName());
					empDTO.setOfficeCity(employeeInfo.getOfficeCity());
					empDTO.setOfficeState(employeeInfo.getOfficeState());
					empDTO.setStatus(employeeInfo.getStatus());
					empDTO.setUpdatedDate(employeeInfo.getUpdatedDate());
					empDTO.setJourneyStatus(employeeInfo.getJourneyStatus());
					empDTO.setfName(employeeInfo.getfName());
					empDTO.setmName(employeeInfo.getmName());
					empDTO.setlName(employeeInfo.getlName());
					empDTO.setEmail(employeeInfo.getEmail());
					empDTO.setFatherName(employeeInfo.getFatherName());
					empDTO.setBankName(employeeInfo.getBankName());
					empDTO.setIfscCode(employeeInfo.getIfscCode());
					empDTO.setOfficePincode(employeeInfo.getOfficePincode());
					empDTO.setOfficeAddress1(employeeInfo.getOfficeAddress1());
					empDTO.setOfficeAddress2(employeeInfo.getOfficeAddress2());
					empDTO.setOfficeAddress3(employeeInfo.getOfficeAddress3());
					empDTO.setMonthlySalary(
							employeeInfo.getMonthlySalary() != null ? employeeInfo.getMonthlySalary().toString()
									: null);
					empDTO.setDesignationCode(employeeInfo.getDesignationCode());

					String decryptedEmpDOB = encryptionDecryptionService.decryptUsingSK(employeeInfo.getDateOfBirth(),
							EncryptionHelper.getUserSKUuids(employeeInfo.getUserSecrets()));
					empDTO.setDateOfBirth(decryptedEmpDOB);

					String decryptedMobile = encryptionDecryptionService.decryptUsingSK(employeeInfo.getMobile(),
							EncryptionHelper.getUserSKUuids(employeeInfo.getUserSecrets()));
					empDTO.setMobileNumber(decryptedMobile);

					String decryptedBankAccNo = encryptionDecryptionService.decryptUsingSK(
							employeeInfo.getBankAccountNo(),
							EncryptionHelper.getUserSKUuids(employeeInfo.getUserSecrets()));
					empDTO.setBankAccountNo(decryptedBankAccNo);

					if (employeeInfo.getFcmToken() != null && !employeeInfo.getFcmToken().isEmpty()) {
						empDTO.setFcmSet(PortalConstant.YES);
					} else {
						empDTO.setFcmSet(PortalConstant.NO);
					}

					List<LenderRecord> lenderRecordsExist = lenderRecordRepository
							.getLenderRecordsByCustomerCodeAndEmployeeCode(employeeInfo.getContract().getCode(),
									employeeInfo.getCode(), PortalConstant.VALID);
					if (lenderRecordsExist != null && !lenderRecordsExist.isEmpty()) {
						logger.info("Employee Lender Records Found");
						empDTO.setCifSet(PortalConstant.YES);
					} else {
						empDTO.setCifSet(PortalConstant.NO);
					}

					empDetailsDTOs.add(empDTO);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", empDetailsPage.getTotalElements());
				responseMap.put("data", empDetailsDTOs);
				stopWatch.stop();
				logger.info("get Employee Details Success");
				logger.info(String.format("Time taken on get Employee Details Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Employee Details Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("get Employee Details failed");
				logger.info(String.format("Employee Records Not Found :- %s", payload.getContractCode()));
				logger.info(String.format("Time taken on get Employee Details Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Employee Details Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Employee Details Service.", e);
			logger.info(
					String.format("Time taken on get Employee Details Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Employee Details Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> addEmployee(EmployeeUploadRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(
				String.format("Start of Upload File service %s, %s", payload.getContractCode(), payload.getUserId()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Contract contract = contractRepository.findByCode(payload.getContractCode());

			CustomerMaster customerMaster = null;

			if (contract != null) {
				customerMaster = contract.getCustomerMaster();
			}

			if (customerMaster != null && contract != null) {
				String bucketName = EMPLOYEE_UPLOAD_FILE_BUCKET;
				String excelScreen = FirebaseScreen.EMPLOYEEEXCEL;
				String originalFileName = PortalConstant.EMPLOYEEEXCELWITHFILETYPE;
				String generatedFileName = payload.getEmpData().get(PortalConstant.EMPCODE) + "-" + new Date().getTime()
						+ "-" + originalFileName;
				String custCode = contract.getCode();
				String fileName = excelScreen + "/" + custCode + "/" + generatedFileName;
				byte[] fileContent = getFileContentByEmpJson(payload.getEmpData());
				cloudStorageService.uploadFile(bucketName, fileName, fileContent);
				ExcelUpload excelUpload = new ExcelUpload();
				excelUpload.setCustomerMaster(customerMaster);
				excelUpload.setUserId(Long.parseLong(payload.getUserId()));
				excelUpload.setCreatedBy(payload.getUserId());
				excelUpload.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				excelUpload.setUpdatedBy(payload.getUserId());
				excelUpload.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				excelUpload.setExcelUploadType(PortalConstant.EMPLOYEEEXCELUPLOAD);
				excelUpload.setFilePath(FirebaseScreen.GOOGLESTORAGE + bucketName + "/" + fileName);
				excelUpload.setGeneratedFileName(generatedFileName);
				excelUpload.setOriginalFileName(originalFileName);
				excelUpload.setStatus(PortalConstant.EXCELUPLOADSTATUS);
				excelUpload.setContract(contract);
				excelUploadRepository.save(excelUpload);
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Upload File Success");
				logger.info(String.format("Time taken on Upload File Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Upload File Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Upload File failed");
				logger.info("Records Not Found");
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

	private byte[] getFileContentByEmpJson(Map<String, String> empData) throws IOException, ParseException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(PortalConstant.EMPLOYEEEXCEL);

		Row headerRow = sheet.createRow(0);
		Row dataRow = sheet.createRow(1);

		int i = 0;
		for (Map.Entry<String, String> entry : empData.entrySet()) {
			if (entry.getKey().equals(PortalConstant.MOBILENO) || entry.getKey().equals(PortalConstant.OFFICEPINCODE)
					|| entry.getKey().equals(PortalConstant.BANKACCNO)) {
				headerRow.createCell(i).setCellValue(entry.getKey());
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					dataRow.createCell(i).setCellValue(Long.parseLong(entry.getValue()));
				}
			} else if (entry.getKey().equals(PortalConstant.MONTHLYSALARY)) {
				headerRow.createCell(i).setCellValue(entry.getKey());
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					dataRow.createCell(i).setCellValue(Double.parseDouble(entry.getValue()));
				}
			} else {
				headerRow.createCell(i).setCellValue(entry.getKey());
				dataRow.createCell(i).setCellValue(entry.getValue());
			}
			i++;
		}
		i = 0;

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();

		return out.toByteArray();
	}
}
