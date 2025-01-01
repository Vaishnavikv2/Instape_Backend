package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.ContractValues;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 24-Jun-2024
 * @ModifyDate - 24-Jun-2024
 * @Desc -
 */
public interface ContractValueRepository extends JpaRepository<ContractValues, Long> {

	@Query(value = "SELECT cv from ContractValues cv where cv.contract.code =:contractCode AND cv.serviceAttribute =:serviceAttribute")
	public ContractValues findByContractCodeAndServiceAttribute(@Param("contractCode") String contractCode,
			@Param("serviceAttribute") String serviceAttribute);

	@Query(value = "SELECT cv from ContractValues cv where cv.contract.code = :contractCode")
	public List<ContractValues> getContractValuesByContractCode(@Param("contractCode") String contractCode);

}
