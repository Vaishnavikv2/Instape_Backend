package com.instape.app.utils;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import com.instape.app.cloudsql.model.LenderEndPointsDTO;
import com.instape.app.cloudsql.model.PartnerServiceEndPointsDTO;
import com.instape.app.cloudsql.model.PartnerServiceValuesDTO;
import com.instape.app.cloudsql.model.RoutingConfigDTO;
import com.instape.app.cloudsql.model.PartnerConstantsDTO;
import com.instape.app.cloudsql.model.PartnerContactsDTO;
import com.instape.app.cloudsql.model.PartnerNotesDTO;
import com.instape.app.cloudsql.model.RulesDTO;
import com.instape.app.cloudsql.model.RulevariablesDTO;
import com.instape.app.request.APITestCaseRequestPayload;
import com.instape.app.request.AddRuleSetsRequestPayload;
import com.instape.app.request.AssignContractRequestPayload;
import com.instape.app.request.AssignOfferRequestPayload;
import com.instape.app.request.BankMasterRequestPayload;
import com.instape.app.request.BookBloodTestRequestPayload;
import com.instape.app.request.CommonRequestPayload;
import com.instape.app.request.ContractOffersRequestPayload;
import com.instape.app.request.ContractUpdateAgreementRequestPayload;
import com.instape.app.request.ContractUpdateAttendanceRequestPayload;
import com.instape.app.request.ContractUpdateControlRequestPayload;
import com.instape.app.request.ContractUpdateLoanRequestPayload;
import com.instape.app.request.ContractUpdateServicesRequestPayload;
import com.instape.app.request.ContractUpdateTNCRequestPayload;
import com.instape.app.request.ContractValuesUpdateRequestPayload;
import com.instape.app.request.CreateContractRequestPayload;
import com.instape.app.request.CreateCustomerRequestPayload;
import com.instape.app.request.CreateOfferRequestPayload;
import com.instape.app.request.EmployeeFetchRequestPayload;
import com.instape.app.request.EmployeeSearchByMobile;
import com.instape.app.request.EmployeeUploadRequestPayload;
import com.instape.app.request.ExecuteTestCaseRequestPayload;
import com.instape.app.request.FileUploadRequestPayload;
import com.instape.app.request.LenderEndPointsRequestPayload;
import com.instape.app.request.PartnerServiceEndPointsRequestPayload;
import com.instape.app.request.PartnerServiceValuesRequestPayload;
import com.instape.app.request.LoanCancelRequestPayload;
import com.instape.app.request.LoanSummaryRequestPayload;
import com.instape.app.request.OptionMasterRequestPayload;
import com.instape.app.request.PartnerAPIsRequestPayload;
import com.instape.app.request.PartnerConstantsRequestPayload;
import com.instape.app.request.PartnerContactsRequestPayload;
import com.instape.app.request.PartnerContractRequestPayload;
import com.instape.app.request.PartnerNotesRequestPayload;
import com.instape.app.request.PaymentConfirmationFileRequestPayload;
import com.instape.app.request.PublishDemandFileRequestPayload;
import com.instape.app.request.RegisterPartnerRequestPayload;
import com.instape.app.request.RejectLoanCancelRequestPayload;
import com.instape.app.request.RotateTokenRequestPayload;
import com.instape.app.request.RulesRequestPayload;
import com.instape.app.request.SearchEmployeeRequestPayload;
import com.instape.app.request.TemplateRequestPayload;
import com.instape.app.request.UpdateContractOfferPayload;
import com.instape.app.request.UpdateCustomerRequestPayload;
import com.instape.app.request.UpdateLoanStatusRequestPayload;
import com.instape.app.request.UpdateOfferRequestPayload;
import com.instape.app.request.UpdateTestCaseExecutionRequestPayload;
import com.instape.app.request.VariableRequestPayload;
import com.instape.app.request.WorkflowRunRequestPayload;
import com.instape.app.response.AccessKeyResponsePayload;
import com.instape.app.response.WorkflowResponsePayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Nov-2023
 * @ModifyDate - 23-Nov-2023
 * @Desc -
 */
public class ValidatePayloadHelper {

	static final Logger logger = LogManager.getFormatterLogger(ValidatePayloadHelper.class);

	public static Map<String, String> checkPayload(CommonRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getEmployerId() == null || payload.getEmployerId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployerId);
		} else if (payload.getEmployeeId() == null || payload.getEmployeeId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployeeId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkAddClientPayloads(MultipartFile file, String custId, String contractCode,
			String userId, String fileType) {
		Map<String, String> map = new HashMap<String, String>();
		if (file == null || file.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideFile);
		} else if (custId == null || custId.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCustomerId);
		} else if (contractCode == null || contractCode.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (userId == null || userId.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (fileType == null || fileType.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideFileType);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkEmployeeSearchByMobile(EmployeeSearchByMobile payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getCustId() == null || payload.getCustId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCustomerId);
		} else if (payload.getCustCode() == null || payload.getCustCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCustomerCode);
		} else if (payload.getMobileNumber() == null || payload.getMobileNumber().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideMobileNumber);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkCreateContractRequestPayload(CreateContractRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getLenderId() == null || payload.getLenderId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankId);
		} else if (payload.getCustomerId() == null || payload.getCustomerId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCustomerId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkCreateCustomerRequestPayload(CreateCustomerRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getCustomerCode() == null || payload.getCustomerCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCustomerCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getCity() == null || payload.getCity().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCity);
		} else if (payload.getState() == null || payload.getState().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideState);
		} else if (payload.getCustomerName() == null || payload.getCustomerName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCustomerName);
		} else if (payload.getContactPersonAddress() == null || payload.getContactPersonAddress().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContactPersonAddress);
		} else if (payload.getContactPersonEmail() == null || payload.getContactPersonEmail().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContactPersonEmail);
		} else if (payload.getContactPersonPhone() == null || payload.getContactPersonPhone().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContactPersonMobile);
		} else if (payload.getContactPersonName() == null || payload.getContactPersonName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContactPersonName);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkUpdateCustomerRequestPayload(UpdateCustomerRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getCustId() == null || payload.getCustId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCustomerId);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getCity() == null || payload.getCity().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCity);
		} else if (payload.getState() == null || payload.getState().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideState);
		} else if (payload.getCustomerName() == null || payload.getCustomerName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCustomerName);
		} else if (payload.getContactPersonAddress() == null || payload.getContactPersonAddress().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContactPersonAddress);
		} else if (payload.getContactPersonEmail() == null || payload.getContactPersonEmail().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContactPersonEmail);
		} else if (payload.getContactPersonPhone() == null || payload.getContactPersonPhone().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContactPersonMobile);
		} else if (payload.getContactPersonName() == null || payload.getContactPersonName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContactPersonName);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkWorkflowRunRequestPayload(WorkflowRunRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if ((payload != null && payload.getMobile() != null && !payload.getMobile().isEmpty())
				|| (payload != null && payload.getCustId() != null && !payload.getCustId().isEmpty()
						&& (payload.getEmployeeId() != null && !payload.getEmployeeId().isEmpty()))) {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		} else {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		}
		return map;
	}

	public static Map<String, String> checkBankMasterRequestPayload(BankMasterRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getBankCode() == null || payload.getBankCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankCode);
		} else if (payload.getBankName() == null || payload.getBankName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankName);
		} else if (payload.getBankLogoPth() == null || payload.getBankLogoPth().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankLogoPath);
		} else if (payload.getCinNumber() == null || payload.getCinNumber().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCinNumber);
		} else if (payload.getContactBankAddress() == null || payload.getContactBankAddress().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankAddress);
		} else if (payload.getContactBankEmail() == null || payload.getContactBankEmail().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankEmail);
		} else if (payload.getContactBankPhone() == null || payload.getContactBankPhone().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankPhone);
		} else if (payload.getGstNumber() == null || payload.getGstNumber().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideGstNumber);
		} else if (payload.getWebsite() == null || payload.getWebsite().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideWebsite);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkBankMasterRequestPayload(BankMasterRequestPayload payload, String bankId) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getBankCode() == null || payload.getBankCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankCode);
		} else if (payload.getBankName() == null || payload.getBankName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankName);
		} else if (payload.getBankLogoPth() == null || payload.getBankLogoPth().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankLogoPath);
		} else if (payload.getCinNumber() == null || payload.getCinNumber().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCinNumber);
		} else if (payload.getContactBankAddress() == null || payload.getContactBankAddress().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankAddress);
		} else if (payload.getContactBankEmail() == null || payload.getContactBankEmail().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankEmail);
		} else if (payload.getContactBankPhone() == null || payload.getContactBankPhone().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankPhone);
		} else if (payload.getGstNumber() == null || payload.getGstNumber().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideGstNumber);
		} else if (payload.getWebsite() == null || payload.getWebsite().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideWebsite);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkCreateOfferRequestPayload(CreateOfferRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getOfferCode() == null || payload.getOfferCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOfferCode);
		} else if (payload.getOfferName() == null || payload.getOfferName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOfferName);
		} else if (payload.getOfferDesc() == null || payload.getOfferDesc().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOfferDesc);
		} else if (payload.getOfferType() == null || payload.getOfferType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOfferType);
		} else if (payload.getProductCode() == null || payload.getProductCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideProductCode);
		} else if (payload.getStartDate() == null || payload.getStartDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStartDate);
		} else if (payload.getEndDate() == null || payload.getEndDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEndDate);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkUpdateOfferRequestPayload(UpdateOfferRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getOfferId() == null || payload.getOfferId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOfferId);
		} else if (payload.getOfferName() == null || payload.getOfferName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOfferName);
		} else if (payload.getOfferDesc() == null || payload.getOfferDesc().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOfferDesc);
		} else if (payload.getProductCode() == null || payload.getProductCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideProductCode);
		} else if (payload.getStartDate() == null || payload.getStartDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStartDate);
		} else if (payload.getEndDate() == null || payload.getEndDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEndDate);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> EmployeeFetchRequestPayload(EmployeeFetchRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getMobile() == null || payload.getMobile().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideMobileNumber);
		} else if (payload.getCustId() == null || payload.getCustId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCustomerId);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getOfferCode() == null || payload.getOfferCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOfferCode);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkAssignOfferRequestPayload(AssignOfferRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getOfferCode() == null || payload.getOfferCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOfferCode);
		} else if (payload.getStartDate() == null || payload.getStartDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStartDate);
		} else if (payload.getEndDate() == null || payload.getEndDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEndDate);
		} else if (payload.getData() == null || payload.getData().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkContractOffersRequestPayload(ContractOffersRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkUpdateContractOfferPayload(UpdateContractOfferPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractOfferId() == null || payload.getContractOfferId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractOfferId);
		} else if (payload.getCampCode() == null || payload.getCampCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCampCode);
		} else if (payload.getStartDate() == null || payload.getStartDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStartDate);
		} else if (payload.getEndDate() == null || payload.getEndDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEndDate);
		} else if (payload.getPackageDetails() == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePackageDetails);
		} else if (payload.getTriggerType() == null || payload.getTriggerType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideTriggerType);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkBookBloodTestRequestPayload(BookBloodTestRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getCustId() == null || payload.getCustId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCustomerId);
		} else if (payload.getEmpId() == null || payload.getEmpId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployeeId);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getMobile() == null || payload.getMobile().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideMobileNumber);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getOfferCode() == null || payload.getOfferCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOfferCode);
		} else if (payload.getAge() == null || payload.getAge().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAge);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkWorkflowResponsePayload(WorkflowResponsePayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkContractAgreementUpdateRequestPayload(
			ContractUpdateAgreementRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getContractStartDate() == null || payload.getContractStartDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractStartDate);
		} else if (payload.getContractEndDate() == null || payload.getContractEndDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractEndDate);
		} else if (payload.getDescription() == null || payload.getDescription().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDescription);
		} else if (payload.getContractName() == null || payload.getContractName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractName);
		} else if (payload.getBankInterest() == null || payload.getBankInterest().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankInterest);
		} else if (payload.getInstallmentDay() == null || payload.getInstallmentDay().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideInstallmentDay);
		} else if (payload.getBlackOutDays() == null || payload.getBlackOutDays().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBlackOutDays);
		} else if (payload.getBlackOutPeriodRequired() == null || payload.getBlackOutPeriodRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBlackOutPeriodRequired);
		} else if (payload.getLoanApprovalRequired() == null || payload.getLoanApprovalRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanApprovalNeeded);
		} else if (payload.getCumulativeLimit() == null || payload.getCumulativeLimit().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCumulativeLimit);
		} else if (payload.getLoanCancelationAccIFSC() == null || payload.getLoanCancelationAccIFSC().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanCancelationAccIFSC);
		} else if (payload.getLoanCancelationAccName() == null || payload.getLoanCancelationAccName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanCancelationAccName);
		} else if (payload.getLoanCancelationAccNo() == null || payload.getLoanCancelationAccNo().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanCancelationAccNo);
		} else if (payload.getOccupation() == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOccupation);
		} else if (payload.getSalaryBand() == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideSalaryBand);
		} else if (payload.getDemandPaymentAccountIFSC() == null || payload.getDemandPaymentAccountIFSC().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDemandPaymentAccountIFSC);
		} else if (payload.getDemandPaymentAccountName() == null || payload.getDemandPaymentAccountName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDemandPaymentAccountName);
		} else if (payload.getDemandPaymentAccountNo() == null || payload.getDemandPaymentAccountNo().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDemandPaymentAccountNo);
		} else if (payload.getIndividualOutstandingLoanLimit() == null
				|| payload.getIndividualOutstandingLoanLimit().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideIndividualOutstandingLoanLimit);
		} else if (payload.getLoanApprovalRequired().equals(PortalConstant.YES)) {
			if (payload.getData() == null || payload.getData().isEmpty()) {
				map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanApproverPayload);
			} else {
				map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
			}
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkContractServicesUpdateRequestPayload(
			ContractUpdateServicesRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getAttendanceServiceStatus() == null || payload.getAttendanceServiceStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAttendanceServiceStatus);
		} else if (payload.getAdvanceServiceStatus() == null || payload.getAdvanceServiceStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAdvanceServiceStatus);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkContractLoanUpdateRequestPayload(ContractUpdateLoanRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getEligibilityCalculationBasis() == null
				|| payload.getEligibilityCalculationBasis().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEligibilityCalculationBasis);
		} else if (payload.getLoanMode() == null || payload.getLoanMode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanMode);
		} else if (payload.getBaseDeductionType() == null || payload.getBaseDeductionType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBaseDeductionType);
		} else if (payload.getBaseDeductionValue() == null || payload.getBaseDeductionValue().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBaseDeductionValue);
		} else if (payload.getIndividualLimit() == null || payload.getIndividualLimit().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideIndividualLimit);
		} else if (payload.getPerDayLoanLimitRequired() == null || payload.getPerDayLoanLimitRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePerDayLoanLimitRequired);
		} else if (payload.getAccrualMode() == null || payload.getAccrualMode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAccrualMode);
		} else if (payload.getPerDayLoanLimitRequired() == null || payload.getPerDayLoanLimitRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePerDayLoanLimitRequired);
		} else if (payload.getOffsetDays() == null || payload.getOffsetDays().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOffsetDays);
		} else if (payload.getPerDayLoanLimitRequired().equals(PortalConstant.YES)) {
			if (payload.getPerDayLoanLimit() == null || payload.getPerDayLoanLimit().isEmpty()) {
				map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePerDayLoanLimit);
			} else {
				map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
			}
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkContractControlUpdateRequestPayload(
			ContractUpdateControlRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getIsInBlackOut() == null || payload.getIsInBlackOut().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideIsInBlackOut);
		} else if (payload.getBlackOutCutOffHrs() == null || payload.getBlackOutCutOffHrs().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBlackOutCutOffHrs);
		} else if (payload.getBlackOutNotificationHrs() == null || payload.getBlackOutNotificationHrs().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBlackOutNotificationHrs);
		} else if (payload.getIncrementStep() == null || payload.getIncrementStep().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideIncrementStep);
		} else if (payload.getMinimumWage() == null || payload.getMinimumWage().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideMinimumWage);
		} else if (payload.getBankNodalOfficerAddress() == null || payload.getBankNodalOfficerAddress().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankNodalOfficerAddress);
		} else if (payload.getBankNodalOfficerContact() == null || payload.getBankNodalOfficerContact().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankNodalOfficerContact);
		} else if (payload.getBankNodalOfficerEmail() == null || payload.getBankNodalOfficerEmail().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankNodalOfficerEmail);
		} else if (payload.getInsNodalOfficerAddress() == null || payload.getInsNodalOfficerAddress().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideInsNodalOfficerAddress);
		} else if (payload.getInsNodalOfficerContact() == null || payload.getInsNodalOfficerContact().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideInsNodalOfficerContact);
		} else if (payload.getInsNodalOfficerEmail() == null || payload.getInsNodalOfficerEmail().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideInsNodalOfficerEmail);
		} else if (payload.getSafeDays() == null || payload.getSafeDays().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideSafeDays);
		} else if (payload.getBankSmileCentreNumber() == null || payload.getBankSmileCentreNumber().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankSmileCentreNumber);
		} else if (payload.getOtpRetryCount() == null || payload.getOtpRetryCount().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOtpRetryCount);
		} else if (payload.getOtpTimeOut() == null || payload.getOtpTimeOut().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOtpTimeOut);
		} else if (payload.getLoginOtpAttempts() == null || payload.getLoginOtpAttempts().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoginOtpAttempts);
		} else if (payload.getMinEligibleDays() == null || payload.getMinEligibleDays().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideMinEligibleDays);
		} else if (payload.getProcessingFees() == null || payload.getProcessingFees().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideProcessingFees);
		} else if (payload.geteSignRequired() == null || payload.geteSignRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideESignRequired);
		} else if (payload.geteSignStampValue() == null || payload.geteSignStampValue().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideESignStampValue);
		} else if (payload.geteSignProfileId() == null || payload.geteSignProfileId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideESignProfileId);
		} else if (payload.getByPassLenderOnboarding() == null || payload.getByPassLenderOnboarding().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideByPassLenderOnboarding);
		} else if (payload.getLivenessRequired() == null || payload.getLivenessRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLivenessRequried);
		} else if (payload.getDisplayEmpBankInfo() == null || payload.getDisplayEmpBankInfo().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDisplayEmpBankInfo);
		} else if (payload.getDisableLocation() == null || payload.getDisableLocation().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDisableLocation);
		} else if (payload.getOfferEligibilityRequired() == null || payload.getOfferEligibilityRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOfferEligibilityRequired);
		} else if (payload.getDisplayPanNameInput() == null || payload.getDisplayPanNameInput().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDisplayPanNameInput);
		} else if (payload.getFirstLoanMaxAmount() == null || payload.getFirstLoanMaxAmount().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideFirstLoanMaxAmount);
		} else if (payload.getvKycByPass() == null || payload.getvKycByPass().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideVKycByPass);
		} else if (payload.getNeedVKycAtOnboardingRequired() == null
				|| payload.getNeedVKycAtOnboardingRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideNeedVKycAtOnboardingRequired);
		} else if (payload.getNumberOfLoanVKycRequired() == null || payload.getNumberOfLoanVKycRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideNumberOfLoanVKycRequired);
		} else if (payload.getAmountOfLoanVKycRequired() == null || payload.getAmountOfLoanVKycRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAmountOfLoanVKycRequired);
		} else if (payload.getIsInMaintenance() == null || payload.getIsInMaintenance().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideIsInMaintenance);
		} else if (payload.getEmpUploadMode() == null || payload.getEmpUploadMode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmpUploadMode);
		} else if (payload.getLoanAccessEnabled() == null || payload.getLoanAccessEnabled().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanAccessEnabled);
		} else if (payload.getRekycValidationMonths() == null || payload.getRekycValidationMonths().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideRekycValidationMonths);
		} else if (payload.getAutoDebitMandateRequired() == null || payload.getAutoDebitMandateRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAutoDebitMandateRequired);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkContractAttendanceUpdateRequestPayload(
			ContractUpdateAttendanceRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getAttendanceUnit() == null || payload.getAttendanceUnit().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAttendanceUnit);
		} else if (payload.getHrsInDay() == null || payload.getHrsInDay().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideHrsInDay);
		} else if (payload.getNoticePeriodThreshold() == null || payload.getNoticePeriodThreshold().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideNoticePeriodThreshold);
		} else if (payload.getDaysInMonth() == null || payload.getDaysInMonth().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDaysInMonth);
		} else if (payload.getRequiredHours() == null || payload.getRequiredHours().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideRequiredhours);
		} else if (payload.getSameLocationOnly() == null || payload.getSameLocationOnly().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideSameLocationOnly);
		} else if (payload.getSameClientOnly() == null || payload.getSameClientOnly().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideSameClientOnly);
		} else if (payload.getPrimaryClientOnly() == null || payload.getPrimaryClientOnly().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePrimaryClientOnly);
		} else if (payload.getLimitedClientOnly() == null || payload.getLimitedClientOnly().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLimitedClientOnly);
		} else if (payload.getLimitedLocationOnly() == null || payload.getLimitedLocationOnly().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLimitedLocationOnly);
		} else if (payload.getCountAttendancePrimaryClientOnly() == null
				|| payload.getCountAttendancePrimaryClientOnly().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCountAttendancePrimaryClientOnly);
		} else if (payload.getEnforceRulesForPunch() == null || payload.getEnforceRulesForPunch().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEnforceRulesForPunch);
		} else if (payload.getAttendanceMaxHrs() == null || payload.getAttendanceMaxHrs().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAttendanceMaxHrs);
		} else if (payload.getAttendanceMinHrs() == null || payload.getAttendanceMinHrs().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAttendanceMinHrs);
		} else if (payload.getRadius() == null || payload.getRadius().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideRadius);
		} else if (payload.getLiveness() == null || payload.getLiveness().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLiveness);
		} else if (payload.getServerTimestamp() == null || payload.getServerTimestamp().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideServerTimestamp);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkContractTNCUpdateRequestPayload(ContractUpdateTNCRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getBankOnboardingTNC() == null || payload.getBankOnboardingTNC().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankOnboardingTNC);
		} else if (payload.getLenderAgreementTNC() == null || payload.getLenderAgreementTNC().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLendergAreementTNC);
		} else if (payload.getLoanApplyPNP() == null || payload.getLoanApplyPNP().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanApplyPNP);
		} else if (payload.getLoanApplyTNC() == null || payload.getLoanApplyTNC().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanApplyTNC);
		} else if (payload.getNameTNC() == null || payload.getNameTNC().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideNameTNC);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkContractValuesUpdateRequestPayload(
			ContractValuesUpdateRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getContractValues() == null || payload.getContractValues().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractValues);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkRegisterPartnerRequestPayload(RegisterPartnerRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getPartnerCode() == null || payload.getPartnerCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerCode);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else if (payload.getPartnerName() == null || payload.getPartnerName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideName);
		} else if (payload.getPartnerType() == null || payload.getPartnerType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideType);
		} else if (payload.getStartDate() == null || payload.getStartDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStartDate);
		} else if (payload.getEndDate() == null || payload.getEndDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEndDate);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getLogoPath() == null || payload.getLogoPath().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLogoPath);
		} else if (payload.getCinNumber() == null || payload.getCinNumber().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCinNumber);
		} else if (payload.getPartnerAddress() == null || payload.getPartnerAddress().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerAddress);
		} else if (payload.getPartnerEmail() == null || payload.getPartnerEmail().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerEmail);
		} else if (payload.getPartnerPhone() == null || payload.getPartnerPhone().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerPhone);
		} else if (payload.getGstNumber() == null || payload.getGstNumber().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideGstNumber);
		} else if (payload.getWebsite() == null || payload.getWebsite().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideWebsite);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkRegisterPartnerRequestPayload(RegisterPartnerRequestPayload payload,
			String action) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getPartnerCode() == null || payload.getPartnerCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerCode);
		} else if (payload.getDeleted() == null || payload.getDeleted().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeleted);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else if (payload.getPartnerName() == null || payload.getPartnerName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideName);
		} else if (payload.getPartnerType() == null || payload.getPartnerType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideType);
		} else if (payload.getStartDate() == null || payload.getStartDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStartDate);
		} else if (payload.getEndDate() == null || payload.getEndDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEndDate);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getLogoPath() == null || payload.getLogoPath().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLogoPath);
		} else if (payload.getCinNumber() == null || payload.getCinNumber().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCinNumber);
		} else if (payload.getPartnerAddress() == null || payload.getPartnerAddress().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerAddress);
		} else if (payload.getPartnerEmail() == null || payload.getPartnerEmail().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerEmail);
		} else if (payload.getPartnerPhone() == null || payload.getPartnerPhone().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerPhone);
		} else if (payload.getGstNumber() == null || payload.getGstNumber().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideGstNumber);
		} else if (payload.getWebsite() == null || payload.getWebsite().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideWebsite);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkRotateTokenRequestPayload(RotateTokenRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getPartnerCode() == null || payload.getPartnerCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerCode);
		} else if (payload.getRotateToken() == null || payload.getRotateToken().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideRotateToken);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkRotateTokenRequestPayload(RotateTokenRequestPayload payload, String action) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getPartnerCode() == null || payload.getPartnerCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkAccessKeyResponsePayload(AccessKeyResponsePayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else if (payload.getDeleted() == null || payload.getDeleted().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeleted);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeleted);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkAssignContractRequestPayload(AssignContractRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getPartnerCode() == null || payload.getPartnerCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerCode);
		} else if (payload.getData() == null || payload.getData().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkPartnerContractRequestPayload(PartnerContractRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getPartnerContractId() == null || payload.getPartnerContractId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerContractId);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else if (payload.getDeleted() == null || payload.getDeleted().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeleted);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkpartnerUploadFile(MultipartFile file, String fileType, String partnerCode,
			String name, String desc, String userId, String originalFileName) {
		Map<String, String> map = new HashMap<String, String>();
		if (file == null || file.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideFile);
		} else if (fileType == null || fileType.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideFileType);
		} else if (partnerCode == null || partnerCode.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerCode);
		} else if (name == null || name.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideName);
		} else if (desc == null || desc.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDesc);
		} else if (userId == null || userId.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (originalFileName == null || originalFileName.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOriginalFileName);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkUpdateFileInfoRequestPayload(String fileType, String id, String userId,
			String status, String deleted, String name, String desc, String originalFileName) {
		Map<String, String> map = new HashMap<String, String>();
		if (id == null || id.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else if (name == null || name.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideName);
		} else if (desc == null || desc.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDesc);
		} else if (userId == null || userId.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (status == null || status.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else if (deleted == null || deleted.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeleted);
		} else if (originalFileName == null || originalFileName.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOriginalFileName);
		} else if (fileType == null || fileType.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideFileType);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkCreatePartnerConstantsRequestPayload(
			PartnerConstantsRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getConstantName() == null || payload.getConstantName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideConstantName);
		} else if (payload.getConstantValue() == null || payload.getConstantValue().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideConstantValue);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getPartnerCode() == null || payload.getPartnerCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerCode);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkPartnerConstantsDTO(PartnerConstantsDTO payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getConstantName() == null || payload.getConstantName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideConstantName);
		} else if (payload.getConstantValue() == null || payload.getConstantValue().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideConstantValue);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else if (payload.getDeleted() == null || payload.getDeleted().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeleted);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkPartnerContactsRequestPayload(PartnerContactsRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getAbout() == null || payload.getAbout().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAbout);
		} else if (payload.getDesignation() == null || payload.getDesignation().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDesignation);
		} else if (payload.getEmail() == null || payload.getEmail().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmail);
		} else if (payload.getName() == null || payload.getName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideName);
		} else if (payload.getOwnership() == null || payload.getOwnership().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOwnership);
		} else if (payload.getPartnerCode() == null || payload.getPartnerCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getPhone() == null || payload.getPhone().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideMobileNumber);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkPartnerContactsDTO(PartnerContactsDTO payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getAbout() == null || payload.getAbout().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAbout);
		} else if (payload.getDesignation() == null || payload.getDesignation().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDesignation);
		} else if (payload.getEmail() == null || payload.getEmail().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmail);
		} else if (payload.getName() == null || payload.getName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideName);
		} else if (payload.getOwnership() == null || payload.getOwnership().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOwnership);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getPhone() == null || payload.getPhone().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideMobileNumber);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkLenderEndPointsRequestPayload(LenderEndPointsRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getData() == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideData);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkLenderEndPointsDTO(LenderEndPointsDTO payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getDeleted() == null || payload.getDeleted().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeleted);
		} else if (payload.getKey() == null || payload.getKey().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideKey);
		} else if (payload.getLenderEndPoint() == null || payload.getLenderEndPoint().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLenderEndPoint);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkPartnerNotesRequestPayload(PartnerNotesRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getPartnerCode() == null || payload.getPartnerCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getDesc() == null || payload.getDesc().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDesc);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkPartnerNotesDTO(PartnerNotesDTO payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getDesc() == null || payload.getDesc().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDesc);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else if (payload.getDeleted() == null || payload.getDeleted().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeleted);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkPartnerAPIsRequestPayload(PartnerAPIsRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getPartnerCode() == null || payload.getPartnerCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getApiKeyRequired() == null || payload.getApiKeyRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideApiKeyRequired);
		} else if (payload.getApiName() == null || payload.getApiName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideApiName);
		} else if (payload.getApiOwner() == null || payload.getApiOwner().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideApiOwner);
		} else if (payload.getAuthTokenRequired() == null || payload.getAuthTokenRequired().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideAuthTokenRequired);
		} else if (payload.getIsInternal() == null || payload.getIsInternal().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideIsInternal);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkAPITestCaseRequestPayload(APITestCaseRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getPartnerCode() == null || payload.getPartnerCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getApiId() == null || payload.getApiId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideApiId);
		} else if (payload.getDeleted() == null || payload.getDeleted().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeleted);
		} else if (payload.getJson() == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideJson);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else if (payload.getIndex() == null || payload.getIndex().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideIndex);
		} else if (payload.getUuid() == null || payload.getUuid().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUUID);
		} else if (payload.getTestCaseName() == null || payload.getTestCaseName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideTestCaseName);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkLoanSummaryRequestPayload(LoanSummaryRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getEmployerId() == null || payload.getEmployerId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployerId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkSearchEmployeeRequestPayload(SearchEmployeeRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getEmployerId() == null || payload.getEmployerId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployerId);
		} else if (payload.getEmployeeName() == null || payload.getEmployeeName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployeeName);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkCancelLoanUploadProof(MultipartFile file, String fileType, String employerId,
			String employeeId, String loanId, String name, String desc, String userId) {
		Map<String, String> map = new HashMap<String, String>();
		if (file == null || file.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideFile);
		} else if (fileType == null || fileType.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideFileType);
		} else if (employerId == null || employerId.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployerId);
		} else if (employeeId == null || employeeId.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployeeId);
		} else if (loanId == null || loanId.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanId);
		} else if (name == null || name.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideName);
		} else if (desc == null || desc.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDesc);
		} else if (userId == null || userId.isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkLoanCancelRequestPayload(LoanCancelRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getEmployerId() == null || payload.getEmployerId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployerId);
		} else if (payload.getEmployeeId() == null || payload.getEmployeeId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployeeId);
		} else if (payload.getLoanId() == null || payload.getLoanId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkRejectLoanCancelRequestPayload(RejectLoanCancelRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getEmployerId() == null || payload.getEmployerId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployerId);
		} else if (payload.getEmployeeId() == null || payload.getEmployeeId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployeeId);
		} else if (payload.getLoanId() == null || payload.getLoanId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanId);
		} else if (payload.getReason() == null || payload.getReason().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideReason);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkFileUploadRequestPayload(FileUploadRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getCustId() == null || payload.getCustId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideCustomerId);
		} else if (payload.getFileType() == null || payload.getFileType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideFileType);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkOptionMasterRequestPayload(OptionMasterRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getOptionDesc() == null || payload.getOptionDesc().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDesc);
		} else if (payload.getOptionName() == null || payload.getOptionName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideName);
		} else if (payload.getOptionType() == null || payload.getOptionType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideType);
		} else if (payload.getOptionValue() == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOptionValue);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkOptionMasterUpdateRequestPayload(OptionMasterRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getOptionDesc() == null || payload.getOptionDesc().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDesc);
		} else if (payload.getOptionName() == null || payload.getOptionName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideName);
		} else if (payload.getOptionType() == null || payload.getOptionType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideType);
		} else if (payload.getOptionValue() == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideOptionValue);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkVariableRequestPayload(VariableRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getVariableName() == null || payload.getVariableName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideVariableName);
		} else if (payload.getVariableType() == null || payload.getVariableType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideVariableType);
		} else if (payload.getSourceType() == null || payload.getSourceType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideSourceType);
		} else if (payload.getVariableDesc() == null || payload.getVariableDesc().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideVariableDesc);
		} else if (payload.getSourceCode() == null || payload.getSourceCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideSourceCode);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkRulevariablesDTO(RulevariablesDTO payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getVariableName() == null || payload.getVariableName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideVariableName);
		} else if (payload.getVariableType() == null || payload.getVariableType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideVariableType);
		} else if (payload.getSourceType() == null || payload.getSourceType().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideSourceType);
		} else if (payload.getDesc() == null || payload.getDesc().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideVariableDesc);
		} else if (payload.getSourceCode() == null || payload.getSourceCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideSourceCode);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkRulesRequestPayload(RulesRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getRuleDescription() == null || payload.getRuleDescription().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDescription);
		} else if (payload.getPriority() == null || payload.getPriority().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePriority);
		} else if (payload.getDeductionPercent() == null || payload.getDeductionPercent().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeductionPercent);
		} else if (payload.getMode() == null || payload.getMode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideMode);
		} else if (payload.getSourceCode() == null || payload.getSourceCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideSourceCode);
		} else if (payload.getDepthCount() == null || payload.getDepthCount().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDepthCount);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getRuleSetId() == null || payload.getRuleSetId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideRuleSetId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkRulesDTO(RulesDTO payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else if (payload.getEnforceStatus() == null || payload.getEnforceStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEnforceStatus);
		} else if (payload.getRuleDesc() == null || payload.getRuleDesc().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDescription);
		} else if (payload.getPriority() == null || payload.getPriority().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePriority);
		} else if (payload.getDeductionPercentage() == null || payload.getDeductionPercentage().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeductionPercent);
		} else if (payload.getMode() == null || payload.getMode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideMode);
		} else if (payload.getSourceCode() == null || payload.getSourceCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideSourceCode);
		} else if (payload.getDepthCount() == null || payload.getDepthCount().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDepthCount);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkExecuteTestCaseRequestPayload(ExecuteTestCaseRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getTestCaseId() == null || payload.getTestCaseId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else if (payload.getJson() == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideJson);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkUpdateTestCaseExecutionRequestPayload(
			UpdateTestCaseExecutionRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getUuid() == null || payload.getUuid().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUUID);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else if (payload.getLogPath() == null || payload.getLogPath().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLogPath);
		} else if (payload.getRespPath() == null || payload.getRespPath().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideResponsePath);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkTemplateRequestPayload(TemplateRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getTemplates() == null || payload.getTemplates().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideTemplateJson);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkAddRuleSetsRequestPayload(AddRuleSetsRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getRuleSetName() == null || payload.getRuleSetName().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideRuleSetName);
		} else if (payload.getRuleSetValue() == null || payload.getRuleSetValue().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideRuleSetValue);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkPublishDemandFileRequestPayload(PublishDemandFileRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else if (payload.getDailyDemandFileId() == null || payload.getDailyDemandFileId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDailyDemandFileId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkEmployeeUploadPayload(EmployeeUploadRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getEmpData() == null || payload.getEmpData().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEmployeeData);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkUpdateLoanStatusRequestPayload(UpdateLoanStatusRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getBankLoanId() == null || payload.getBankLoanId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideBankLoanId);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getLoanStatus() == null || payload.getLoanStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideLoanStatus);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkPaymentConfirmationFileRequestPayload(
			PaymentConfirmationFileRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getStartDate() == null || payload.getStartDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStartDate);
		} else if (payload.getEndDate() == null || payload.getEndDate().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideEndDate);
		} else if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideContractCode);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkPartnerServiceEndPointsRequestPayload(
			PartnerServiceEndPointsRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getData() == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideData);
		} else if (payload.getPartnerServiceId() == null || payload.getPartnerServiceId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerServiceId);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkPartnerServiceEndPointsDTO(PartnerServiceEndPointsDTO payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getDeleted() == null || payload.getDeleted().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeleted);
		} else if (payload.getKey() == null || payload.getKey().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideKey);
		} else if (payload.getPartnerServiceEndPoint() == null || payload.getPartnerServiceEndPoint().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerServiceEndPoint);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkPartnerServiceValuesRequestPayload(
			PartnerServiceValuesRequestPayload payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getData() == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideData);
		} else if (payload.getPartnerServiceId() == null || payload.getPartnerServiceId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerServiceId);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		return map;
	}

	public static Map<String, String> checkPartnerServiceValuesDTO(PartnerServiceValuesDTO payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getDeleted() == null || payload.getDeleted().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideDeleted);
		} else if (payload.getKey() == null || payload.getKey().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideKey);
		} else if (payload.getValue() == null || payload.getValue().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideValue);
		} else if (payload.getStatus() == null || payload.getStatus().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideStatus);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkRoutingConfigDTO(RoutingConfigDTO payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getProductId() == null || payload.getProductId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideProductId);
		} else if (payload.getPartnerId() == null || payload.getPartnerId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerId);
		} else if (payload.getPartnerServiceId() == null || payload.getPartnerServiceId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePartnerServiceId);
		} else if (payload.getScreen() == null || payload.getScreen().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideScreen);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

	public static Map<String, String> checkUpdateRoutingConfigDTO(RoutingConfigDTO payload) {
		Map<String, String> map = new HashMap<String, String>();
		if (payload == null) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvidePayload);
		} else if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideUserId);
		} else if (payload.getId() == null || payload.getId().isEmpty()) {
			map.put(PortalConstant.ERROR, CommonMessageLog.ProvideId);
		} else {
			map.put(PortalConstant.RESPONSE, CommonMessageLog.SUCCESS);
		}
		return map;
	}

}
