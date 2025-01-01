package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.BusinessRules;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 26-Mar-2024
 * @ModifyDate - 26-Mar-2024
 * @Desc -
 */
public interface BusinessRulesRepository extends JpaRepository<BusinessRules, Long> {

	@Query(value = "SELECT r from BusinessRules r where r.ruleSets.id =:ruleSetId AND r.status =:status order by r.createdDate asc")
	public List<BusinessRules> findByRuleSetIdAndStatus(@Param("ruleSetId") String ruleSetId,
			@Param(PortalConstant.STATUS) String status);

}
