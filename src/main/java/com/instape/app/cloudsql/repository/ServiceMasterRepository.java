package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.ServiceMaster;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 20-Jun-2024
 * @ModifyDate - 20-Jun-2024
 * @Desc -
 */
public interface ServiceMasterRepository extends JpaRepository<ServiceMaster, Long> {

	@Query(value = "SELECT s from ServiceMaster s where s.serviceName = :serviceName")
	public ServiceMaster getServiceByServiceName(@Param("serviceName") String serviceName);

}
