package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.instape.app.cloudsql.model.OffersAvailMapping;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 10-Jul-2024
 * @ModifyDate - 10-Jul-2024
 * @Desc -
 */
public interface OffersAvailMappingRepository extends JpaRepository<OffersAvailMapping, Long> {

	@Query("SELECT oam FROM OffersAvailMapping oam WHERE oam.employeeInfo.code =:empCode AND oam.contractOffers.offers.offerCode = :offerCode AND oam.contractOffers.contract.code = :contractCode")
	public OffersAvailMapping getEmployeeAvailedOfferRecord(String contractCode, String offerCode, String empCode);

}
