package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.WorkflowRun;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 27-Nov-2023
 * @ModifyDate - 27-Nov-2023
 * @Desc -
 */
public interface WorkflowRunRepository extends JpaRepository<WorkflowRun, Long>,JpaSpecificationExecutor<WorkflowRun> {

	@Query(value = "SELECT w from WorkflowRun w where w.contract.code = :employerCode  AND w.employeeInfo.code =:employeeCode")
	List<WorkflowRun> getWorkflowRunsByCustomerCodeAndEmployeeCode(@Param("employerCode") String employerCode,
			@Param("employeeCode") String employeeCode) throws Exception;

	@Query(value = "SELECT w.* from workflow_run w where w.function_name=?2 and w.request_payload -> 'data' -> 'LoanData' ->> 'DisbursementUid'=?1 order by w.created_date desc limit 1", nativeQuery = true)
	public List<WorkflowRun> getLoanInformationByBankLoanIdAndFunctionName(@Param("DisbursementUid") String loanId,
			String functionName);

	@Query(value = "SELECT w1.emp_id FROM workflow_run w1 WHERE w1.status = 'error' AND w1.record_status = 'valid' AND w1.contract_code = :contractCode AND w1.function_name IN (:onboardingFunctions) AND w1.created_date = (SELECT MAX(created_date) FROM workflow_run w2 WHERE w2.emp_id = w1.emp_id AND w2.record_status = 'valid') GROUP BY w1.emp_id ORDER BY MAX(w1.created_date) DESC", nativeQuery = true)
	List<String> getInErrorCountByCustomerCode(@Param("contractCode") String custCode,
			@Param("onboardingFunctions") List<String> onboardingfunctions);

	@Query(value = "SELECT w from WorkflowRun w where w.employeeInfo.code =:employeeCode AND w.recordStatus = :valid ORDER BY w.createdDate DESC")
	Page<WorkflowRun> getWorkflowRunsByEmployeeCode(@Param("employeeCode") String employeeCode, @Param(PortalConstant.VALID) String valid,
			PageRequest pageRequest) throws Exception;

	@Query(value = "SELECT w from WorkflowRun w where w.contract.code = :employerCode  AND w.employeeInfo.code =:employeeCode AND w.recordStatus = :valid")
	List<WorkflowRun> getWorkflowRunsByCustomerCodeAndEmployeeCode(@Param("employerCode") String employerCode,
			@Param("employeeCode") String employeeCode, @Param(PortalConstant.VALID) String valid) throws Exception;

}
