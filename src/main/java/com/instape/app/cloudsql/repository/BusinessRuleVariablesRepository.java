package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.instape.app.cloudsql.model.BusinessRuleVariables;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 15-Mar-2024
 * @ModifyDate - 15-Mar-2024
 * @Desc -
 */
public interface BusinessRuleVariablesRepository extends JpaRepository<BusinessRuleVariables, Long> {

	@Query(value = "SELECT r from BusinessRuleVariables r where r.variableName ilike:variableName AND r.contract.code =:contractCode AND r.status =:status")
	public BusinessRuleVariables findVariableByNameAndContractCode(@Param("variableName") String variableName,
			@Param("contractCode") String contractCode, @Param(PortalConstant.STATUS) String status);

	@Query(value = "SELECT r from BusinessRuleVariables r where r.contract.code =:contractCode AND r.status =:status order by r.createdDate asc")
	public List<BusinessRuleVariables> findByContractCodeAndStatus(@Param("contractCode") String contractCode,
			@Param(PortalConstant.STATUS) String status);

	@Query(value = "SELECT r from BusinessRuleVariables r where r.id =:variableId")
	public BusinessRuleVariables findVariablesById(@RequestParam("variableId") Long variableId);
}
