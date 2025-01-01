package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.LoanRecord;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Dec-2023
 * @ModifyDate - 07-Dec-2023
 * @Desc -
 */
public interface LoanRecordRepository extends JpaRepository<LoanRecord, Long> {

	@Query(value = "SELECT SUM(l.amount) from loan_record l where l.contract_id = :contractId AND l.loan_status = :loanStatus", nativeQuery = true)
	public Double getTotalLoanAmountByCustomerCode(@Param("contractId") String custCode,
			@Param(PortalConstant.LOANSTATUS) String status);

	@Query(value = "SELECT l from LoanRecord l where l.bankLoanId =:bankLoanId")
	public LoanRecord getLoanRecordByBankLoanId(@Param("bankLoanId") String bankLoanId) throws Exception;
}
