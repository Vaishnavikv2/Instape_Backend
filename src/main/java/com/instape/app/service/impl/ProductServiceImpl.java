package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.instape.app.cloudsql.model.ContractProducts;
import com.instape.app.cloudsql.model.Partner;
import com.instape.app.cloudsql.model.PartnerServiceDTO;
import com.instape.app.cloudsql.model.ProductCatalog;
import com.instape.app.cloudsql.model.ProductDTO;
import com.instape.app.cloudsql.model.ProductListDTO;
import com.instape.app.cloudsql.model.ProductPartnerServiceDTO;
import com.instape.app.cloudsql.repository.ContractProductRepository;
import com.instape.app.cloudsql.repository.PartnerRepository;
import com.instape.app.cloudsql.repository.ProductCatalogRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.response.PageResponse;
import com.instape.app.response.PageableResponseDTO;
import com.instape.app.service.ProductService;
import com.instape.app.utils.StatusCode;

@Service
public class ProductServiceImpl implements ProductService {
	private final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductCatalogRepository productRepository;

	@Autowired
	private PartnerRepository partnerRepository;
	
	@Autowired
	private ContractProductRepository contractProductRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public ProductDTO create(Long loggedInUser, ProductDTO productDto) {
		logger.info("Creating Product");
		if (productRepository.existsByName(productDto.getName())) {
			logger.error("Product already exists with same name, Name:{}", productDto.getName());
			throw new InstapeException("Product already exists with same name", StatusCode.ALREADY_EXIST);
		}
		ProductCatalog product = mapper.map(productDto, ProductCatalog.class);
		product.setCreatedBy(loggedInUser);
		product.setUpdatedBy(loggedInUser);
		product.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		product.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		product.setDeleted(false);
		product = productRepository.save(product);
		return mapper.map(product, ProductDTO.class);
	}

	@Override
	public ProductDTO update(Long loggedInUser, ProductDTO productDto) {
		logger.info("updating product");
		ProductCatalog product = productRepository.getProductById(productDto.getId());
		if (product == null) {
			throw new InstapeException("Product does not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		if (productRepository.existsByNameForUpdate(productDto.getName(), product.getId())) {
			throw new InstapeException("Product with same name already exist", HttpStatus.BAD_REQUEST.value());
		}
		product.setName(productDto.getName());
		product.setDescription(productDto.getDescription());
		// product.setCode(productDto.getCode());
		product.setUpdatedBy(loggedInUser);
		product = productRepository.save(product);
		return mapper.map(product, ProductDTO.class);
	}

	@Override
	public PageableResponseDTO getAll(String searchtext, final Pageable pageable) {
		logger.info("getting product  list");
		Page<ProductCatalog> page = null;
		if (StringUtils.hasText(searchtext)) {
			page = productRepository.getAllProductWithSearch(searchtext, pageable);
		} else {
			page = productRepository.getAllProduct(pageable);
		}
		List<ProductDTO> productDtoList = page.getContent().stream()
				.map(resource -> mapper.map(resource, ProductDTO.class)).collect(Collectors.toList());
		PageResponse pagination = new PageResponse(page.getTotalElements(), page.getPageable().getPageNumber(),
				page.getNumberOfElements(), page.getSize());
		return new PageableResponseDTO(productDtoList, pagination);
	}

	@Override
	public ProductDTO getById(Long productId) {
		logger.info("getting product by id");
		ProductCatalog product = productRepository.getProductById(productId);
		if (product == null) {
			throw new InstapeException("Product does not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		return mapper.map(product, ProductDTO.class);
	}

	@Override
	public void delete(Long loggedInUser, Long productId) {
		logger.info("deleting resources");
		ProductCatalog product = productRepository.getProductById(productId);
		if (product == null) {
			throw new InstapeException("Product does not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		product.setDeleted(true);
		product.setUpdatedBy(loggedInUser);
		productRepository.save(product);

	}

	@Override
	public boolean isExistWithSameName(String productName) {
		logger.info("checking Product exisitance");
		return productRepository.existsByName(productName);
	}

	@Override
	public List<ProductListDTO> getAll() {
		logger.info("getting product  list");
		return productRepository.getAllProduct();
	}

	@Override
	public List<ProductPartnerServiceDTO> getProductPartnerServices(Long productId) {
		List<Partner> partnerList = partnerRepository.getAllPartnerByProductId(productId);
		List<ProductPartnerServiceDTO> response = new ArrayList<>();
		for (Partner partner : partnerList) {
			ProductPartnerServiceDTO partnerDto = mapper.map(partner, ProductPartnerServiceDTO.class);
			List<PartnerServiceDTO> serviceList = partner.getPartnerServices().stream()
					.filter(s->s.isDeleted()!=true).map(s -> mapper.map(s, PartnerServiceDTO.class)).toList();
			partnerDto.setServices(serviceList);
			response.add(partnerDto);
		}
		return response;
	}

	@Override
	public List<ProductPartnerServiceDTO> getContractProductPartnerServices(Long contractProductId) {
		ContractProducts contractProducts = contractProductRepository
				.getContractProductById(contractProductId);
		if (contractProducts == null) {
			throw new InstapeException("Product mapping didn't exist", StatusCode.ENTITY_NOT_FOUND);
		}
		List<Partner> partnerList = partnerRepository.getAllPartnerByProductId(contractProducts.getProductCatalog().getId());
		List<ProductPartnerServiceDTO> response = new ArrayList<>();
		for (Partner partner : partnerList) {
			ProductPartnerServiceDTO partnerDto = mapper.map(partner, ProductPartnerServiceDTO.class);
			List<PartnerServiceDTO> serviceList = partner.getPartnerServices().stream()
					.filter(s->s.isDeleted()!=true).map(s -> mapper.map(s, PartnerServiceDTO.class)).toList();
			partnerDto.setServices(serviceList);
			response.add(partnerDto);
		}
		return response;
	}
}