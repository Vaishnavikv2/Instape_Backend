package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.instape.app.cloudsql.model.Contract;
import com.instape.app.cloudsql.model.ContractPartnerServiceDTO;
import com.instape.app.cloudsql.model.ContractProductDTO;
import com.instape.app.cloudsql.model.ContractProductPartnerService;
import com.instape.app.cloudsql.model.ContractProducts;
import com.instape.app.cloudsql.model.PartnerServices;
import com.instape.app.cloudsql.model.ProductCatalog;
import com.instape.app.cloudsql.repository.CPPartnerServiceRepository;
import com.instape.app.cloudsql.repository.ContractProductRepository;
import com.instape.app.cloudsql.repository.ContractRepository;
import com.instape.app.cloudsql.repository.PartnerServiceRepository;
import com.instape.app.cloudsql.repository.ProductCatalogRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.firebase.service.FirestoreServiceHandler;
import com.instape.app.service.ContractProductService;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.FirebaseScreen;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StatusCode;
import com.instape.app.utils.UserStatus;

@Service
public class ContractProductServiceImpl implements ContractProductService {
	private final Logger logger = LogManager.getLogger(ContractProductServiceImpl.class);

	@Autowired
	private ContractProductRepository contractProductRepository;

	@Autowired
	private PartnerServiceRepository partnerServiceRepository;

	@Autowired
	private CPPartnerServiceRepository cpPartnerServiceRepository;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private ProductCatalogRepository productRepository;

	@Autowired
	private FirestoreServiceHandler fireStoreServiceHandler;

	@Value("${FIRESTORE_ROOT}")
	private String firestoreRoot;

	@Override
	public List<ContractProductDTO> getAll(String contractcode) {
		List<ContractProducts> contractProductList = contractProductRepository.getAllContractProducts(contractcode);
		return contractProductList.stream().map(cp -> new ContractProductDTO(cp.getId(), cp.getProductCatalog().getId(),
				cp.getProductCatalog().getName(), cp.getStatus())).toList();
	}

	@Override
	public ContractProductDTO create(String contractcode, ContractProductDTO contractProductDto, Long loggedInUser) {
		Contract contract = contractRepository.getcontractByContractCode(contractcode);
		if (contract == null) {
			throw new InstapeException("Contract didn't exist", StatusCode.ENTITY_NOT_FOUND);
		}
		ProductCatalog product = productRepository.getProductById(contractProductDto.getProductId());
		if (product == null) {
			throw new InstapeException("Product didn't exist", StatusCode.ENTITY_NOT_FOUND);
		}
		if (isExistWithSameProduct(contractcode, contractProductDto.getProductId())) {
			throw new InstapeException("Contract already have same product", HttpStatus.BAD_REQUEST.value());
		}
		ContractProducts contractProducts = new ContractProducts();
		contractProducts.setContract(contract);
		contractProducts.setProductCatalog(product);
		contractProducts.setStatus(UserStatus.ACTIVE.getValue());
		contractProducts.setDeleted(false);
		contractProducts.setCreatedBy(loggedInUser);
		contractProducts.setUpdatedBy(loggedInUser);
		contractProducts.setCreatedDate(DateUtils.getCurrentTimestamp());
		contractProducts.setUpdatedDate(DateUtils.getCurrentTimestamp());
		contractProducts = contractProductRepository.save(contractProducts);
		contractProductDto.setId(contractProducts.getId());
		contractProductDto.setProductName(product.getName());
		contractProductDto.setStatus(UserStatus.ACTIVE.getValue());
		addNewProductOnFireStore(contractcode, product.getCode());
		return contractProductDto;
	}

	@Override
	public void delete(Long contractProductId, Long loggedInUser) {
		ContractProducts contractProducts = contractProductRepository.getContractProductById(contractProductId);
		if (contractProducts == null) {
			throw new InstapeException("Product mapping didn't exist", StatusCode.ENTITY_NOT_FOUND);
		}
		contractProducts.setDeleted(true);
		contractProducts.setStatus(UserStatus.DELETED.getValue());
		contractProducts.setUpdatedDate(DateUtils.getCurrentTimestamp());
		contractProducts.setUpdatedBy(loggedInUser);
		contractProductRepository.save(contractProducts);
		deleteProductOnFireStore(contractProducts.getContract().getCode(),
				contractProducts.getProductCatalog().getCode());
	}

	@Override
	public boolean isExistWithSameProduct(String contractcode, Long productId) {
		return contractProductRepository.existsByProductId(contractcode, productId);
	}

	@Override
	public void create(String contractCode, List<Long> productIds, Long loggedInUserId) {
		Contract contract = contractRepository.getcontractByContractCode(contractCode);
		if (contract == null) {
			throw new InstapeException("Contract didn't exist", StatusCode.ENTITY_NOT_FOUND);
		}

		Map<Long, ProductCatalog> productMap = new HashMap<>();
		List<Long> notFoundProducts = new ArrayList<Long>();
		List<String> existProduct = new ArrayList<>();
		for (Long productId : productIds) {
			ProductCatalog product = productRepository.getProductById(productId);
			if (product == null) {
				notFoundProducts.add(productId);
				continue;
			}
			if (isExistWithSameProduct(contractCode, productId)) {
				existProduct.add(product.getName());
				continue;
			}
			productMap.put(productId, product);
		}
		if (!notFoundProducts.isEmpty()) {
			throw new InstapeException("All Selected Product didn't exist", StatusCode.ENTITY_NOT_FOUND);
		}
		if (!existProduct.isEmpty()) {
			throw new InstapeException("Contract already have same products: " + existProduct.toString(),
					StatusCode.ENTITY_NOT_FOUND);
		}
		List<ContractProducts> contractProductsList = new ArrayList<>();
		for (Long productId : productIds) {
			ContractProducts contractProducts = new ContractProducts();
			contractProducts.setContract(contract);
			contractProducts.setProductCatalog(productMap.get(productId));
			contractProducts.setDeleted(false);
			contractProducts.setStatus(UserStatus.ACTIVE.getValue());
			contractProducts.setCreatedBy(loggedInUserId);
			contractProducts.setUpdatedBy(loggedInUserId);
			contractProducts.setCreatedDate(DateUtils.getCurrentTimestamp());
			contractProducts.setUpdatedDate(DateUtils.getCurrentTimestamp());
			contractProductsList.add(contractProducts);
		}
		contractProductRepository.saveAll(contractProductsList);
		// Add Product to firebase under this contract
		contractProductsList.stream().forEach(cp -> {
			addNewProductOnFireStore(contractCode, cp.getProductCatalog().getCode());
		});
	}

	@Override
	public ContractProductDTO update(String contractCode, ContractProductDTO contractProductDto, Long loggedInUserId) {
		ContractProducts contractProducts = contractProductRepository
				.getContractProductById(contractProductDto.getId());
		if (contractProducts == null) {
			throw new InstapeException("Product mapping didn't exist", StatusCode.ENTITY_NOT_FOUND);
		}
		contractProducts.setStatus(contractProductDto.getStatus());
		contractProducts.setUpdatedBy(loggedInUserId);
		contractProducts.setUpdatedDate(DateUtils.getCurrentTimestamp());
		contractProductRepository.save(contractProducts);
		contractProductDto.setProductId(contractProducts.getProductCatalog().getId());
		contractProductDto.setProductName(contractProducts.getProductCatalog().getName());
		// update product status on fs
		updateProductStatusOnFireStore(contractCode, contractProducts.getProductCatalog().getCode(),
				contractProductDto.getStatus());
		return contractProductDto;
	}

	@Override
	public List<ContractPartnerServiceDTO> createLenderServiceMapping(String contractCode, Long cpId,
			List<Long> contractServiceList, Long loggedInUserId) {
		Contract contract = contractRepository.getcontractByContractCode(contractCode);
		if (contract == null) {
			throw new InstapeException("Contract didn't exist", StatusCode.ENTITY_NOT_FOUND);
		}
		ContractProducts contractProducts = contractProductRepository.getContractProductById(cpId);
		if (contractProducts == null) {
			throw new InstapeException("Product mapping didn't exist", StatusCode.ENTITY_NOT_FOUND);
		}
		List<ContractProductPartnerService> cplsUpdatedList = new ArrayList<>();
		List<ContractProductPartnerService> allExistingCplsList = cpPartnerServiceRepository
				.getAllCPPartnerService(contractCode, cpId);
		Map<Long, PartnerServices> cplsMap = new HashMap<>();
		List<Long> validPartnerIds = new ArrayList<>();
		List<Long> validPartnerServiceIds = new ArrayList<>();
		for (Long partnerServiceId : contractServiceList) {
			PartnerServices partnerService = partnerServiceRepository.getPartnerServiceById(partnerServiceId);
			if (partnerService == null) {
				logger.error("Partner Service not found while mapping service in contract, serviceid:{}",
						partnerServiceId);
			} else {
				validPartnerIds.add(partnerService.getPartner().getId());
				validPartnerServiceIds.add(partnerServiceId);
				cplsMap.put(partnerServiceId, partnerService);
			}
		}

		List<Long> exisitngActivePartnerIds = allExistingCplsList.stream()
				.filter(cpls -> cpls.getStatus().equals(UserStatus.ACTIVE.getValue()))
				.map(cpls -> cpls.getPartnerServices().getPartner().getId()).toList();
		List<Long> tobeInactiveMarkPartnerIds = exisitngActivePartnerIds.stream()
				.filter(l -> !validPartnerIds.contains(l)).toList();
		List<Long> tobeInactivePartnerServiceIds = new ArrayList<>();
		// to be mark as inactive
		allExistingCplsList.stream()
				.filter(cpls -> tobeInactiveMarkPartnerIds.contains(cpls.getPartnerServices().getPartner().getId()))
				.forEach(cpls -> {
					cpls.setStatus(UserStatus.INACTIVE.getValue());
					cpls.setUpdatedDate(DateUtils.getCurrentTimestamp());
					cpls.setUpdatedBy(loggedInUserId);
					cplsUpdatedList.add(cpls);
					tobeInactivePartnerServiceIds.add(cpls.getPartnerServices().getId());
				});
		// remove all Inactive marked Ids
		validPartnerServiceIds.removeAll(tobeInactivePartnerServiceIds);
		for (Long lenderServiceId : validPartnerServiceIds) {
			PartnerServices partnerService = cplsMap.get(lenderServiceId);
			ContractProductPartnerService lastService = allExistingCplsList.stream().filter(
					cpls -> cpls.getPartnerServices().getPartner().getId()
					.equals(partnerService.getPartner().getId())&&cpls.getContractProducts().getId()==cpId)
					.findFirst().orElse(null);
			if (lastService != null) {
				lastService.setPartnerServices(partnerService);
				lastService.setStatus(UserStatus.ACTIVE.getValue());
				lastService.setUpdatedDate(DateUtils.getCurrentTimestamp());
				lastService.setUpdatedBy(loggedInUserId);
				cplsUpdatedList.add(lastService);
			} else {
				ContractProductPartnerService cpls = new ContractProductPartnerService();
				cpls.setContract(contract);
				cpls.setContractProducts(contractProducts);
				cpls.setPartnerServices(partnerService);
				cpls.setCreatedBy(loggedInUserId);
				cpls.setUpdatedBy(loggedInUserId);
				cpls.setCreatedDate(DateUtils.getCurrentTimestamp());
				cpls.setUpdatedDate(DateUtils.getCurrentTimestamp());
				cpls.setStatus(UserStatus.ACTIVE.getValue());
				cplsUpdatedList.add(cpls);
			}

		}
		if (!cplsUpdatedList.isEmpty()) {
			allExistingCplsList = cpPartnerServiceRepository.saveAll(cplsUpdatedList);
		}
		cplsUpdatedList.stream().filter(cpls -> UserStatus.ACTIVE.getValue().equals(cpls.getStatus())).forEach(cpls -> {
			updateProductPartnerServiceOnFireStore(contractCode,contractProducts, cpls);
		});
		return getAllLenderService(contractCode, cpId);
	}

	@Override
	public List<ContractPartnerServiceDTO> getAllLenderService(String contractCode, Long cpId) {
		List<ContractProductPartnerService> cplsList = cpPartnerServiceRepository.getAllCPPartnerService(contractCode,
				cpId);
		return covertToDTO(cplsList);
	}

	private List<ContractPartnerServiceDTO> covertToDTO(List<ContractProductPartnerService> cplsList) {
		List<ContractPartnerServiceDTO> cplsDtoList = new ArrayList<>();
		cplsList.stream().forEach(cpls -> {
			ContractPartnerServiceDTO cplsDto = new ContractPartnerServiceDTO();
			cplsDto.setId(cpls.getId());
			cplsDto.setPartnerId(cpls.getPartnerServices().getPartner().getId());
			cplsDto.setPartnerName(cpls.getPartnerServices().getPartner().getName());
			cplsDto.setPartnerServiceId(cpls.getPartnerServices().getId());
			cplsDto.setPartnerServiceName(cpls.getPartnerServices().getName());
			cplsDto.setStatus(cpls.getStatus());
			cplsDto.setId(cpls.getId());
			cplsDtoList.add(cplsDto);
		});
		return cplsDtoList;
	}

	private void addNewProductOnFireStore(String contractId, String productCode) {
		Map<String, Object> product = new HashMap<>();
		product.put(PortalConstant.PARTNER_CODE, "");
		product.put(PortalConstant.PARTNER_NAME, "");
		product.put(PortalConstant.STATUS, PortalConstant.ACTIVE);
		product.put(PortalConstant.PARTNER_SERVICE_ID, "");
		product.put(FirebaseScreen.TIMESTAMP, Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
		fireStoreServiceHandler.createAndUpdateFSDocument(product, firestoreRoot, FirebaseScreen.INSTAPE,
				FirebaseScreen.EMPLOYERS, contractId, FirebaseScreen.CONTRACT, contractId, FirebaseScreen.Services,
				productCode);
	}

	private void deleteProductOnFireStore(String contractId, String productCode) {
		fireStoreServiceHandler.createAndUpdateFSDocument(null, firestoreRoot, FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS,
				contractId, FirebaseScreen.CONTRACT, contractId, FirebaseScreen.Services, productCode);
	}

	private void updateProductStatusOnFireStore(String contractId, String productCode, String status) {
		Map<String, Object> product = new HashMap<>();
		product.put(PortalConstant.STATUS, status);
		product.put(FirebaseScreen.TIMESTAMP, Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
		fireStoreServiceHandler.createAndUpdateFSDocument(product, firestoreRoot, FirebaseScreen.INSTAPE,
				FirebaseScreen.EMPLOYERS, contractId, FirebaseScreen.CONTRACT, contractId, FirebaseScreen.Services,
				productCode);
	}

	private void updateProductPartnerServiceOnFireStore(String contractCode, ContractProducts contractProduct,ContractProductPartnerService cpls) {
		Map<String, Object> product = new HashMap<>();
		product.put(PortalConstant.PARTNER_CODE, cpls.getPartnerServices().getPartner().getCode());
		product.put(PortalConstant.PARTNER_NAME, cpls.getPartnerServices().getPartner().getPartnerName());
		product.put(PortalConstant.STATUS,contractProduct.getStatus());
		product.put(PortalConstant.PARTNER_SERVICE_ID, cpls.getPartnerServices().getCode());
		product.put(FirebaseScreen.TIMESTAMP, Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
		fireStoreServiceHandler.createAndUpdateFSDocument(product, firestoreRoot, FirebaseScreen.INSTAPE,
				FirebaseScreen.EMPLOYERS, contractCode, FirebaseScreen.CONTRACT, contractCode, FirebaseScreen.Services,
				contractProduct.getProductCatalog().getCode());
	}
}
