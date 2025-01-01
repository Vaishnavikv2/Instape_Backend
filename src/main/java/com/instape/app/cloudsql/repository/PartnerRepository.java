package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.Partner;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 30-Jul-2024
 * @ModifyDate - 30-Jul-2024
 * @Desc -
 */
public interface PartnerRepository extends JpaRepository<Partner, Long> {

	@Query("SELECT p FROM Partner p WHERE p.partnerName ilike :partnerName AND p.isDeleted = false order by p.createdDate desc")
	public Page<Partner> findByPartnerName(String partnerName, PageRequest pageRequest);

	@Query("SELECT p FROM Partner p WHERE p.type ilike :partnerType AND p.isDeleted = false order by p.createdDate desc")
	public Page<Partner> findByPartnerType(String partnerType, PageRequest pageRequest);

	@Query("SELECT p FROM Partner p WHERE p.isDeleted = false order by p.createdDate desc")
	public Page<Partner> findAllPartners(PageRequest pageRequest);

	public Partner findByCode(String partnerCode);
	
	@Query("SELECT l FROM Partner l WHERE l.id = :id AND l.isDeleted<> true")
	Partner getPartnerById(@Param("id") Long id);
	
	@Query("SELECT pp.partner FROM PartnerProductMapping pp where pp.isDeleted <> true "
			+ " and pp.productCatalog.id=:productId ")
	List<Partner> getAllPartnerByProductId(@Param("productId") Long productId);
}
