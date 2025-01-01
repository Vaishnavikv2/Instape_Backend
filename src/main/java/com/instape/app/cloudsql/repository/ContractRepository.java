package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.Contract;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Nov-2023
 * @ModifyDate - 28-Nov-2023
 * @Desc -
 */
public interface ContractRepository extends JpaRepository<Contract, Long> {

	@Query(value = "SELECT c from Contract c where c.code = :contractCode")
	public Contract getcontractByContractCode(@Param("contractCode") String contractCode);

	@Query(value = "SELECT c from Contract c where c.customerMaster.id = :customerId")
	public List<Contract> getcontractByCustomerId(@Param("customerId") Long customerId);

	public Contract findByCode(String code);

	@Query(value = "SELECT c from Contract c where (c.code = :contractCode OR c.customerMaster.customerName ilike :customerName OR c.bankMaster.bankName ilike :bankName) AND c.status = :status order by c.createdDate desc")
	public Page<Contract> getContractsDetailsUsingFilters(String contractCode, String customerName, String bankName,
			PageRequest pageRequest, String status);

	@Query(value = "SELECT c from Contract c where c.status = :status order by c.createdDate desc")
	public Page<Contract> getAllContractDetailsByStatus(String status, PageRequest pageRequest);

	@Query(value = "SELECT c from Contract c where c.status = :status order by c.createdDate desc")
	public List<Contract> getAllContractDetailsByStatus(String status);
}
