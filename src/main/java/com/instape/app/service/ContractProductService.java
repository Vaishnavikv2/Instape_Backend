package com.instape.app.service;

import java.util.List;

import com.instape.app.cloudsql.model.ContractPartnerServiceDTO;
import com.instape.app.cloudsql.model.ContractProductDTO;

public interface ContractProductService {

	List<ContractProductDTO> getAll(String contractcode);

	ContractProductDTO create(String contractcode,ContractProductDTO contractProductDto,Long loggedInUser);

	void delete(Long contractProductId,Long loggedInUser);

	boolean isExistWithSameProduct(String contractcode,Long productId);

	void create(String contractCode, List<Long> productIds, Long loggedInUserId);

	ContractProductDTO update(String contractCode, ContractProductDTO contractProductDto, Long loggedInUserId);

	List<ContractPartnerServiceDTO> createLenderServiceMapping(String contractCode,
			Long cpId, List<Long> contractServiceList, Long loggedInUserId);

	List<ContractPartnerServiceDTO> getAllLenderService(String contractCode, Long cpId);
}
