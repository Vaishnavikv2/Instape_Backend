package com.instape.app.service;

import java.util.List;

import com.instape.app.cloudsql.model.PartnerServiceDTO;

public interface PartnerServicesService {

	List<PartnerServiceDTO> getAll(Long lenderId);

	PartnerServiceDTO getById(Long lenderId,Long Id);

	PartnerServiceDTO create(Long lenderId, PartnerServiceDTO lender,Long loggedInUser);

	PartnerServiceDTO update(Long lenderId,  PartnerServiceDTO lender,Long loggedInUser);

	void delete(Long lenderId,Long loggedInUser);

	boolean isExistWithSameCode(Long lenderId,String lenderServiceCode);

}
