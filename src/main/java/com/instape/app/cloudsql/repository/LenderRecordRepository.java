package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.LenderRecord;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Dec-2023
 * @ModifyDate - 07-Dec-2023
 * @Desc -
 */
public interface LenderRecordRepository extends JpaRepository<LenderRecord, Long> {

	@Query(value = "SELECT l from LenderRecord l where l.contract.code = :employerCode  AND l.employeeInfo.code =:employeeCode AND l.status ilike:status order by l.createdDate desc")
	List<LenderRecord> getLenderRecordsByCustomerCodeAndEmployeeCode(@Param("employerCode") String employerCode,
			@Param("employeeCode") String employeeCode, @Param("status") String status) throws Exception;

	@Query(value = "SELECT COUNT(DISTINCT l.emp_id) from lender_record l where l.contract_id = :contractId", nativeQuery = true)
	Long getOnBoardingCountByCustomerCode(@Param("contractId") String custCode);
}
