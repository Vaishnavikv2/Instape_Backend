package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.CustomerMaster;

public interface CustomerMasterRepository extends JpaRepository<CustomerMaster, Long> {

	@Query(value = "SELECT cm from CustomerMaster cm where cm.id =:custId")
	public CustomerMaster findCustomerMasterByCustomerId(@Param("custId") Long custId);

	public CustomerMaster findByCode(String customerCode);

	@Query(value = "SELECT cm from CustomerMaster cm where cm.status =:status")
	public List<CustomerMaster> getCustomerMastersByStatus(@Param("status") String active);

	@Query(value = "SELECT c from CustomerMaster c where (c.code = :customerCode OR c.customerName ilike :customerName) AND c.status = :status order by c.createdDate desc")
	public Page<CustomerMaster> getCustomersDetailsUsingFilters(String customerCode, String customerName,
			PageRequest pageRequest, String status);

	@Query(value = "SELECT c from CustomerMaster c where c.status = :status order by c.createdDate desc")
	public Page<CustomerMaster> getAllCustomersDetailsByStatus(String status, PageRequest pageRequest);

}
