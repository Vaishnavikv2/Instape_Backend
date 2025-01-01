package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.ManualLoanCancelFileInfo;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 12-Feb-2024
 * @ModifyDate - 12-Feb-2024
 * @Desc -
 */
public interface ManualLoanCancelFileInfoRepository extends JpaRepository<ManualLoanCancelFileInfo, Long> {

	@Query(value = "SELECT m from ManualLoanCancelFileInfo m where m.loanRecord.bankLoanId =:bankLoanId")
	public List<ManualLoanCancelFileInfo> getCancelFileInfosByBankLoanId(@Param("bankLoanId") String bankLoanId);
}
