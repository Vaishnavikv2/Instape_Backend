package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.ManagerApprovers;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Aug-2024
 * @ModifyDate - 28-Aug-2024
 * @Desc -
 */
public interface ManagerApproversRepository extends JpaRepository<ManagerApprovers, Long> {

	@Query(value = "SELECT m FROM ManagerApprovers m WHERE m.contract.code = :contractCode AND m.status ilike :status order by m.createdDate DESC")
	public List<ManagerApprovers> getManagerApproversByContractCode(String contractCode, String status);

	@Query(value = "SELECT m FROM ManagerApprovers m WHERE m.contract.code = :contractCode AND m.employeeInfo.code = :employeeCode")
	public ManagerApprovers findByContractCodeAndEmployeeCode(String contractCode, String employeeCode);

	@Query(value = "SELECT m FROM ManagerApprovers m WHERE m.contract.code = :contractCode AND m.status ilike :status order by m.createdDate DESC")
	public List<ManagerApprovers> findByContractCode(String contractCode, String status);

}
