package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.instape.app.cloudsql.model.PartnerClientContract;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 22-Oct-2024
 * @ModifyDate - 22-Oct-2024
 * @Desc -
 */
public interface PartnerClientContractRepository extends JpaRepository<PartnerClientContract, Long> {

	@Query("SELECT CASE WHEN COUNT(pcc) > 0 THEN FALSE ELSE TRUE END FROM PartnerClientContract pcc WHERE pcc.clientId ilike :clientId")
	boolean checkForUniqueClientId(String clientId);

}
