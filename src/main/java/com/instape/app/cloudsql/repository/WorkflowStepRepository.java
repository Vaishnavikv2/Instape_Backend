package com.instape.app.cloudsql.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.WorkflowStep;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 11-Jul-2024
 * @ModifyDate - 11-Jul-2024
 * @Desc -
 */
public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Long> {

	@Query(value = "SELECT e from WorkflowStep e where e.workflowConfig.processConfig.contractProcessConfig.contract.id =:contractId AND e.functionName=:functionName And  e.workflowConfig.gcpWorkflowId=:workflowName")
	public WorkflowStep getWorkflowStepByContractId(@Param("contractId") Long contractId,
			@Param("functionName") String functionName, @Param("workflowName") String workflowName);

	@Query(value = "SELECT e.functionName from WorkflowStep e where e.workflowConfig.processConfig.contractProcessConfig.contract.code =:contractCode order by e.functionName asc")
	public Set<String> getFunctionsByContractCode(String contractCode);
}
