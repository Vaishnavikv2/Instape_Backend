package com.instape.app.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import com.instape.app.request.BankDemandRequestPayload;
import com.instape.app.request.LoanCancelRequestPayload;
import com.instape.app.request.LoanInfoRequestpayload;
import com.instape.app.request.LoanSummaryRequestPayload;
import com.instape.app.request.PaymentConfirmationFileRequestPayload;
import com.instape.app.request.PublishDemandFileRequestPayload;
import com.instape.app.request.ReconciliationRecordsRequestPayload;
import com.instape.app.request.RejectLoanCancelRequestPayload;
import com.instape.app.request.SearchEmployeeRequestPayload;
import com.instape.app.request.UpdateLoanStatusRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Dec-2023
 * @ModifyDate - 07-Dec-2023
 * @Desc -
 */
public interface LoanSummaryService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 12-Dec-2023
	 * @ModifyDate - 12-Dec-2023
	 * @Desc -
	 */
	public Map<Object, Object> getLoanSummary(LoanSummaryRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 12-Dec-2023
	 * @ModifyDate - 12-Dec-2023
	 * @Desc -
	 */
	public Map<Object, Object> searchEmployee(SearchEmployeeRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 15-Dec-2023
	 * @ModifyDate - 15-Dec-2023
	 * @Desc -
	 */
	public Map<Object, Object> getLoanInformation(LoanInfoRequestpayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 08-Feb-2024
	 * @ModifyDate - 08-Feb-2024
	 * @Desc -
	 */
	public Map<Object, Object> cancelLoanUploadProof(MultipartFile file, String fileType, String employerId,
			String employeeId, String loanId, String name, String desc, String userId);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Feb-2024
	 * @ModifyDate - 09-Feb-2024
	 * @Desc -
	 */
	public Map<Object, Object> getProofUploadList(LoanCancelRequestPayload payload);

	/**
	 * 
	 * 
	 * @param userId
	 * @Author - Nagaraj
	 * @CreationDate - 12-Feb-2024
	 * @ModifyDate - 12-Feb-2024
	 * @Desc -
	 */
	public Map<Object, Object> approveLoanCancelRequest(LoanCancelRequestPayload payload, String userId);

	/**
	 * 
	 * 
	 * @param userId
	 * @Author - Nagaraj
	 * @CreationDate - 12-Feb-2024
	 * @ModifyDate - 12-Feb-2024
	 * @Desc -
	 */
	public Map<Object, Object> rejectLoanCancelRequest(RejectLoanCancelRequestPayload payload, String userId);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 06-Aug-2024
	 * @ModifyDate - 06-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getApproveRejectLogs(LoanCancelRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 06-Aug-2024
	 * @ModifyDate - 06-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> downloadFiles(List<Long> data);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 07-Aug-2024
	 * @ModifyDate - 07-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getBankDemandRecords(BankDemandRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 07-Aug-2024
	 * @ModifyDate - 07-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getReconciliationRecords(ReconciliationRecordsRequestPayload payload,
			PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 17-Sep-2024
	 * @ModifyDate - 17-Sep-2024
	 * @Desc -
	 */
	public Map<Object, Object> getBankDailyDemandRecords(BankDemandRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 27-Sep-2024
	 * @ModifyDate - 27-Sep-2024
	 * @Desc -
	 */
	public Map<Object, Object> downloadDemandFile(String bucketName, String resourcePath);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 01-Oct-2024
	 * @ModifyDate - 01-Oct-2024
	 * @Desc -
	 */
	public Map<Object, Object> publishDemandFile(PublishDemandFileRequestPayload payload, String userId);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 07-Oct-2024
	 * @ModifyDate - 07-Oct-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateLoanStatus(UpdateLoanStatusRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Oct-2024
	 * @ModifyDate - 09-Oct-2024
	 * @Desc -
	 */
	public Map<Object, Object> getPaymentConfirmationFiles(PaymentConfirmationFileRequestPayload payload,
			PageRequest pageRequest);

}
