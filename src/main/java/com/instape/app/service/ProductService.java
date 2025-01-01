package com.instape.app.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.instape.app.cloudsql.model.ProductDTO;
import com.instape.app.cloudsql.model.ProductPartnerServiceDTO;
import com.instape.app.cloudsql.model.ProductListDTO;
import com.instape.app.response.PageableResponseDTO;

public interface ProductService {

	PageableResponseDTO getAll(String searchtext,Pageable pageable);
	
	List<ProductListDTO> getAll();

	ProductDTO getById(Long productId);

	ProductDTO create(Long loggedInUser,ProductDTO product);

	ProductDTO update(Long loggedInUser,ProductDTO product);

	void delete(Long loggedInUser,Long productId);
	
	boolean isExistWithSameName(String productName);

	List<ProductPartnerServiceDTO> getProductPartnerServices(Long productId);
	
	List<ProductPartnerServiceDTO> getContractProductPartnerServices(Long contractProductId);
	
}
