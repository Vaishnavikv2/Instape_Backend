package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.RuleSets;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 03-May-2024
 * @ModifyDate - 03-May-2024
 * @Desc -
 */
public interface RuleSetsRepository extends JpaRepository<RuleSets, Long> {

	@Query(value = "SELECT r from RuleSets r where r.contract.code =:contractCode AND r.status =:status order by r.createdDate asc")
	public List<RuleSets> findByContractCodeAndStatus(@Param("contractCode") String contractCode,
			@Param(PortalConstant.STATUS) String active);

}
