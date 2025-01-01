package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.ContractProductPartnerService;

public interface CPPartnerServiceRepository extends JpaRepository<ContractProductPartnerService, Long> {
	@Query("SELECT cpls FROM ContractProductPartnerService cpls WHERE cpls.contract.code=:contractCode "
			+ " and cpls.contractProducts.id=:cpId and cpls.isDeleted<>true")
	List<ContractProductPartnerService> getAllCPPartnerService(@Param("contractCode") String contractCode,@Param("cpId") Long cpId);
	
	@Query("SELECT cpls FROM ContractProductPartnerService cpls WHERE cpls.id=:cplsId and cpls.isDeleted<>true")
	ContractProductPartnerService getCPPartnerServiceById(@Param("cplsId") Long cplsId);
	
	@Query("SELECT cpls FROM ContractProductPartnerService cpls WHERE cpls.partnerServices.id=:lsid "
			+ " and cpls.contractProducts.id=:cpId and cpls.isDeleted<>true")
	ContractProductPartnerService getCPPartnerServiceByProductIdAndPartnerServiceId(@Param("cpId") Long cpId,@Param("lsid") Long lsid);

	@Query("SELECT cpls FROM ContractProductPartnerService cpls WHERE cpls.partnerServices.partner.id=:partnerId "
			+ " and cpls.contractProducts.id=:cpId and cpls.isDeleted<>true")
	ContractProductPartnerService getCPPartnerServiceByProductIdAndPartnerId(@Param("cpId") Long cpId,@Param("partnerId") Long partnerId);

}