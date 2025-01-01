package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.ContractOffers;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 08-Jul-2024
 * @ModifyDate - 08-Jul-2024
 * @Desc -
 */
public interface ContractOffersRepository extends JpaRepository<ContractOffers, Long> {

	@Query("SELECT c FROM ContractOffers c WHERE c.offers.offerCode = :offerCode AND c.contract.code = :contractCode order by c.createdDate desc")
	List<ContractOffers> findByOfferCode(String offerCode, String contractCode);

	@Query("SELECT c FROM ContractOffers c WHERE c.contract.code = :contractCode order by c.createdDate desc")
	List<ContractOffers> getAllOffers(String contractCode);

	@Query("SELECT c FROM ContractOffers c WHERE c.contract.code = :contractCode AND c.active = :active order by c.createdDate desc")
	List<ContractOffers> getAllOffers(String contractCode, boolean active);

	@Query("SELECT c FROM ContractOffers c WHERE c.contract.code = :contractCode AND c.offers.offerCode = :offerCode AND c.active = :active")
	ContractOffers getContractOfferByContractCodeAndOfferCode(String contractCode, String offerCode, boolean active);

}
