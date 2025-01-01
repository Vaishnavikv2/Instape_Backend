package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.Offers;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Jul-2024
 * @ModifyDate - 04-Jul-2024
 * @Desc -
 */
public interface OffersRepository extends JpaRepository<Offers, Long> {

	@Query("SELECT o FROM Offers o WHERE o.offerCode = :offerCode")
	public Offers findByOfferCode(String offerCode);

	@Query("SELECT o FROM Offers o WHERE o.offerCode = :offerCode")
	public Page<Offers> findByOfferCode(String offerCode, PageRequest pageRequest);

	@Query("SELECT o FROM Offers o order by o.createdDate desc")
	public Page<Offers> getAllOffers(PageRequest pageRequest);

}
