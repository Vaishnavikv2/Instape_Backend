package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.ProcessConfig;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 29-Nov-2023
 * @ModifyDate - 29-Nov-2023
 * @Desc -
 */
public interface ProcessConfigRepository extends JpaRepository<ProcessConfig, Long> {

	@Query(value = "SELECT p from ProcessConfig p where  p.contractProcessConfig.contract.code = :contractCode")
	List<ProcessConfig> getProcessConfigByContractCode(@Param("contractCode") String contractCode) throws Exception;
}
