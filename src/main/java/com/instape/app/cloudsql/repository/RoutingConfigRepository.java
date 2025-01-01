package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.RoutingConfig;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 11-Nov-2024
 * @ModifyDate - 11-Nov-2024
 * @Desc -
 */
public interface RoutingConfigRepository extends JpaRepository<RoutingConfig, Long> {

	@Query("SELECT rc FROM RoutingConfig rc WHERE rc.productCatalog.id = :productId AND rc.partner.id = :partnerId AND rc.partnerServices.id = :partnerServiceId AND rc.screen ilike :screen AND rc.isDeleted = false")
	public RoutingConfig checkForDuplicateRoutingConfig(long productId, long partnerId, long partnerServiceId,
			String screen);

	@Query("SELECT rc FROM RoutingConfig rc WHERE rc.isDeleted = false ORDER BY rc.createdDate DESC")
	public Page<RoutingConfig> getAllRoutingConfigs(PageRequest pageRequest);

}
