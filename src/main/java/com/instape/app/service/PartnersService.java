package com.instape.app.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import com.instape.app.cloudsql.model.PartnerConstantsDTO;
import com.instape.app.cloudsql.model.PartnerContactsDTO;
import com.instape.app.cloudsql.model.PartnerNotesDTO;
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
import com.instape.app.request.TestCaseExecutionsRequestPayload;
import com.instape.app.response.AccessKeyResponsePayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 29-Jul-2024
 * @ModifyDate - 29-Jul-2024
 * @Desc -
 */
public interface PartnersService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 29-Jul-2024
	 * @ModifyDate - 29-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> registerPartner(RegisterPartnerRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 29-Jul-2024
	 * @ModifyDate - 29-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> getPartners(PartnersRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 30-Jul-2024
	 * @ModifyDate - 30-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> updatePartner(RegisterPartnerRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 30-Jul-2024
	 * @ModifyDate - 30-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> rotateToken(RotateTokenRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 30-Jul-2024
	 * @ModifyDate - 30-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> generateAccessKey(RotateTokenRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 30-Jul-2024
	 * @ModifyDate - 30-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> getAccessKeys(RotateTokenRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 30-Jul-2024
	 * @ModifyDate - 30-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateAccessKey(AccessKeyResponsePayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 30-Jul-2024
	 * @ModifyDate - 30-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> getUnassignedContracts(PartnersRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 30-Jul-2024
	 * @ModifyDate - 30-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> assignContract(AssignContractRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 31-Jul-2024
	 * @ModifyDate - 31-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> getPartnerContracts(PartnersRequestPayload payload, PageRequest pageRequest);

	/**
	 *
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 31-Jul-2024
	 * @ModifyDate - 31-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> updatePartnerContract(PartnerContractRequestPayload payload);

	/**
	 * 
	 * 
	 * @param originalFileName
	 * @Author - Nagaraj
	 * @CreationDate - 31-Jul-2024
	 * @ModifyDate - 31-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> partnerUploadFile(MultipartFile file, String fileType, String partnerCode, String name,
			String desc, String userId, String originalFileName);

	/**
	 * 
	 * 
	 * @param pageRequest
	 * @Author - Nagaraj
	 * @CreationDate - 31-Jul-2024
	 * @ModifyDate - 31-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> getFileUploadList(String partnerCode, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 31-Jul-2024
	 * @ModifyDate - 31-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateFileInfo(MultipartFile file, String fileType, String id, String userId,
			String status, String deleted, String name, String desc, String originalFileName);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 01-Aug-2024
	 * @ModifyDate - 01-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> partnerDownloadFiles(List<Long> data);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 01-Aug-2024
	 * @ModifyDate - 01-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> addConstants(PartnerConstantsRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 01-Aug-2024
	 * @ModifyDate - 01-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getConstants(PartnersRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 01-Aug-2024
	 * @ModifyDate - 01-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateConstants(PartnerConstantsDTO payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 02-Aug-2024
	 * @ModifyDate - 02-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> addContacts(PartnerContactsRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 02-Aug-2024
	 * @ModifyDate - 02-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getContacts(PartnersRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 02-Aug-2024
	 * @ModifyDate - 02-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateContacts(PartnerContactsDTO payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 02-Aug-2024
	 * @ModifyDate - 02-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> addNotes(PartnerNotesRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 02-Aug-2024
	 * @ModifyDate - 02-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getNotes(PartnersRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 02-Aug-2024
	 * @ModifyDate - 02-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateNotes(PartnerNotesDTO payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 02-Aug-2024
	 * @ModifyDate - 02-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> addAPIs(PartnerAPIsRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 02-Aug-2024
	 * @ModifyDate - 02-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getAPIs(PartnersRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 05-Aug-2024
	 * @ModifyDate - 05-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> addTestCase(APITestCaseRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 05-Aug-2024
	 * @ModifyDate - 05-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getAPITestCases(PartnerAPIRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 22-Aug-2024
	 * @ModifyDate - 22-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> partnerUploadLocalFile(MultipartFile file, String fileType, String partnerCode,
			String name, String desc, String userId, String originalFileName);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 04-Sep-2024
	 * @ModifyDate - 04-Sep-2024
	 * @Desc -
	 */
	public Map<Object, Object> executeTestCase(ExecuteTestCaseRequestPayload payload,String token);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 04-Sep-2024
	 * @ModifyDate - 04-Sep-2024
	 * @Desc -
	 */
	public Map<Object, Object> getTestCaseExecutions(TestCaseExecutionsRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 04-Sep-2024
	 * @ModifyDate - 04-Sep-2024
	 * @Desc -
	 */
	public Map<Object, Object> downloadExecutionFiles(String path);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 04-Sep-2024
	 * @ModifyDate - 04-Sep-2024
	 * @Desc -
	 */
	public Map<Object, Object> fetchExecutionStatus(String uuid);
}
