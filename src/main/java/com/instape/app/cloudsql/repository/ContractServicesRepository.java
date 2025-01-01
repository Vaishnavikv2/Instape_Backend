package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.ContractService;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 20-Jun-2024
 * @ModifyDate - 20-Jun-2024
 * @Desc -
 */
public interface ContractServicesRepository extends JpaRepository<ContractService, Long> {

	@Query(value = "SELECT cs from ContractService cs where cs.contract.id =:contractId AND cs.serviceMaster.serviceName = :serviceName")
	public ContractService getServiceByServiceType(Long contractId, String serviceName);

}
