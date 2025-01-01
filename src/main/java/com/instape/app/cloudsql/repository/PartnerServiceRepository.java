package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.PartnerServices;

public interface PartnerServiceRepository extends JpaRepository<PartnerServices, Long> {
	@Query("SELECT CASE WHEN COUNT(ps) > 0 THEN TRUE ELSE FALSE END FROM PartnerServices ps WHERE ps.partner.id=:partnerId and ps.code =:code AND ps.isDeleted <>true")
	boolean existsByCode(@Param("partnerId") Long partnerId,@Param("code") String code);
	
	@Query("SELECT CASE WHEN COUNT(ps) > 0 THEN TRUE ELSE FALSE END FROM PartnerServices ps WHERE ps.partner.id=:partnerId "
			+ " and ps.code =:code AND ps.id <> :id AND ps.isDeleted <>true")
	boolean existsByCodeForUpdate(@Param("partnerId") Long partnerId,@Param("id") Long id,@Param("code") String code);
	
	@Query("SELECT ps FROM PartnerServices ps WHERE ps.id = :id AND ps.isDeleted<> true")
	PartnerServices getPartnerServiceById(@Param("id") Long id);

	@Query("SELECT ps FROM PartnerServices ps where ps.partner.id=:partnerId and ps.isDeleted <>true order by ps.createdDate desc")
	List<PartnerServices> getAllPartnerServices(@Param("partnerId") Long partnerId);

}