package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.LoanCancelledSettlement;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 12-Feb-2024
 * @ModifyDate - 12-Feb-2024
 * @Desc -
 */
public interface LoanCancelledSettlementRepository extends JpaRepository<LoanCancelledSettlement, Long> {

	@Query("select l from LoanCancelledSettlement l where l.loanRecord.bankLoanId =:loanId order by l.createdDate desc")
	public List<LoanCancelledSettlement> getLogsByBankLoanId(String loanId);

}
