package com.instape.app.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;
import com.instape.app.cloudsql.model.Contract;
import com.instape.app.cloudsql.model.CustomerMaster;
import com.instape.app.cloudsql.model.ExcelUpload;
import com.instape.app.cloudsql.model.ExcelUploadDTO;
import com.instape.app.cloudsql.repository.ContractRepository;
import com.instape.app.cloudsql.repository.CustomerMasterRepository;
import com.instape.app.cloudsql.repository.ExcelUploadRepository;
import com.instape.app.cloudstore.service.CloudStorageService;
import com.instape.app.request.FileUploadRequestPayload;
import com.instape.app.service.ClientMasterService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.FirebaseScreen;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 11-Jan-2024
 * @ModifyDate - 11-Jan-2024
 * @Desc -
 */
@Service
public class ClientMasterServiceImpl implements ClientMasterService {

	static final Logger logger = LogManager.getFormatterLogger(ClientMasterServiceImpl.class);

	@Value("${EMPLOYEE_UPLOAD_FILE_BUCKET}")
	private String EMPLOYEE_UPLOAD_FILE_BUCKET;

	@Value("${PROJECT_ID}")
	private String projectId;

	@Autowired
	private CloudStorageService cloudStorageService;

	@Autowired
	private CustomerMasterRepository customerMasterRepository;

	@Autowired
	private ExcelUploadRepository excelUploadRepository;

	@Value("classpath:templates/TemplateClientExcelUpload.xlsx")
	Resource clientTemplateFile;

	@Value("classpath:templates/TemplateEmployeeExcelUpload.xlsx")
	Resource empTemplateFile;

	@Autowired
	private ContractRepository contractRepository;

	@Override
	public Map<Object, Object> uploadFile(MultipartFile file, String custId, String contractCode, String userId,
			String fileType) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Upload File service %s, %s, %s", custId, userId, fileType));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			CustomerMaster customerMaster = customerMasterRepository
					.findCustomerMasterByCustomerId(Long.parseLong(custId));
			Contract contract = contractRepository.findByCode(contractCode);
			if (customerMaster != null && contract != null) {
				String bucketName = fileType.equals(PortalConstant.CLIENTEXCELUPLOAD) ? null
						: EMPLOYEE_UPLOAD_FILE_BUCKET;
				String excelScreen = fileType.equals(PortalConstant.CLIENTEXCELUPLOAD) ? FirebaseScreen.CLIENTEXCEL
						: FirebaseScreen.EMPLOYEEEXCEL;
				String originalFileName = file.getOriginalFilename();
				String generatedFileName = new Date().getTime() + "-" + originalFileName;
				String custCode = contract.getCode();
				String fileName = excelScreen + "/" + custCode + "/" + generatedFileName;
				byte[] fileContent = file.getBytes();
				uploadFileToFirebase(fileName, fileContent, bucketName);
				ExcelUpload excelUpload = new ExcelUpload();
				excelUpload.setCustomerMaster(customerMaster);
				excelUpload.setUserId(Long.parseLong(userId));
				excelUpload.setCreatedBy(userId);
				excelUpload.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				excelUpload.setUpdatedBy(userId);
				excelUpload.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				excelUpload.setExcelUploadType(fileType);
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
				logger.info(String.format("Records Not Found :- %s, %s", custId, userId));
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

	public void uploadFileToFirebase(String fileName, byte[] fileContent, String bucketName) throws IOException {
		cloudStorageService.uploadFile(bucketName, fileName, fileContent);
	}

	@Override
	public Map<Object, Object> getFileUploadList(FileUploadRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Get File Uploads service %s, %s, %s", payload.getCustId(),
				payload.getFileType(), payload.getContractCode()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<ExcelUpload> excelUploadPage = excelUploadRepository.getExcelUploadsByExcelUploadTypeAndCustomerId(
					payload.getFileType(), payload.getContractCode(), pageRequest);
			List<ExcelUpload> excelUploadList = excelUploadPage.getContent();
			Contract contract = contractRepository.findByCode(payload.getContractCode());
			String empUploadType = null;
			if (contract != null) {
				empUploadType = contract.getEmpUploadMode();
			}
			if (excelUploadList != null && !excelUploadList.isEmpty()) {
				List<ExcelUploadDTO> excelUploadDTOs = new ArrayList<ExcelUploadDTO>();
				for (ExcelUpload excelUpload : excelUploadList) {
					ExcelUploadDTO dto = new ExcelUploadDTO();
					dto.setFileName(excelUpload.getOriginalFileName());
					dto.setGeneratedFileName(excelUpload.getGeneratedFileName());
					dto.setStatus(excelUpload.getStatus());
					dto.setCreatedDate(excelUpload.getCreatedDate());
					dto.setReason((excelUpload.getReason() != null && !excelUpload.getReason().toString().isEmpty())
							? excelUpload.getReason()
							: null);
					excelUploadDTOs.add(dto);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", excelUploadPage.getTotalElements());
				responseMap.put("data", excelUploadDTOs);
				responseMap.put("empUploadType", empUploadType);
				stopWatch.stop();
				logger.info("Get File Uploads Success");
				logger.info(
						String.format("Time taken on Get File Uploads Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get File Uploads Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", Collections.EMPTY_LIST);
				responseMap.put("empUploadType", empUploadType);
				stopWatch.stop();
				logger.info("Get File Uploads failed");
				logger.info(String.format("Records Not Found :- %s, %s", payload.getCustId(), payload.getFileType()));
				logger.info(
						String.format("Time taken on Get File Uploads Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get File Uploads Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get File Uploads Service.", e);
			logger.info(String.format("Time taken on Get File Uploads Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get File Uploads Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> downloadTemplate(String templateType) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Download Template service %s", templateType));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Workbook workbook = null;
			if (templateType.equals(PortalConstant.CLIENTEXCELUPLOAD)) {
				workbook = new XSSFWorkbook(clientTemplateFile.getInputStream());
			} else {
				workbook = new XSSFWorkbook(empTemplateFile.getInputStream());
			}
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			workbook.close();
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			responseMap.put("data", outputStream.toByteArray());
			stopWatch.stop();
			logger.info("Download Template Success");
			logger.info(
					String.format("Time taken on Download Template Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Download Template Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Download Template Service.", e);
			logger.info(
					String.format("Time taken on Download Template Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Download Template Service.");
			return responseMap;
		}
	}
}
