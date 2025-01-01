package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.instape.app.cloudsql.model.Partner;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 02-Sep-2024
 * @ModifyDate - 02-Sep-2024
 * @Desc -
 */
public interface LenderRepository extends JpaRepository<Partner, Long> {
//	@Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END FROM Lender l WHERE l.code ilike :code AND l.isDeleted <>true")
//	boolean existsByCode(@Param("code") String code);
//
//	@Query("SELECT l FROM Lender l WHERE l.id = :id AND l.isDeleted<> true")
//	Lender getLenderById(@Param("id") Long id);
//
//	@Query("SELECT l FROM Lender l where l.isDeleted <>true order by l.name asc")
//	Page<Lender> getAllLenders(final Pageable pageable);
//	
//	@Query("SELECT lp.lender FROM LenderProductMapping lp where lp.isDeleted <> true "
//			+ " and lp.productCatalog.id=:productId ")
//	List<Lender> getAllLenderByProductId(@Param("productId") Long productId);

}