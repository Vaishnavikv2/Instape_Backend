package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.ExcelUpload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 15-Jan-2024
 * @ModifyDate - 15-Jan-2024
 * @Desc -
 */
public interface ExcelUploadRepository extends JpaRepository<ExcelUpload, Long> {

	@Query(value = "SELECT e from ExcelUpload e where e.excelUploadType = :fileType AND e.contract.code = :contractCode order by e.createdDate desc")
	Page<ExcelUpload> getExcelUploadsByExcelUploadTypeAndCustomerId(@Param("fileType") String fileType,
			@Param("contractCode") String contractCode, PageRequest pageRequest);

}
