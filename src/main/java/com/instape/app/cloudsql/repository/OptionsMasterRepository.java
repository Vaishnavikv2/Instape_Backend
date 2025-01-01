package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.OptionsMaster;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Dec-2023
 * @ModifyDate - 28-Dec-2023
 * @Desc -
 */
public interface OptionsMasterRepository extends JpaRepository<OptionsMaster, Long> {

	@Query(value = "SELECT om from OptionsMaster om where om.optionType =:optionType AND om.status =:status")
	public OptionsMaster getOptionsMastersByOptionTypeAndStatus(@Param("optionType") String optionType,
			@Param("status") String status);

	@Query(value = "SELECT om from OptionsMaster om where om.optionType ilike :optionType AND om.status =:status")
	public OptionsMaster findByOptionMasterNameAndStatus(String optionType, String status);

	@Query(value = "SELECT om from OptionsMaster om where om.status =:status order by om.createdDate desc")
	public Page<OptionsMaster> findByStatus(String status, PageRequest pageRequest);
}
