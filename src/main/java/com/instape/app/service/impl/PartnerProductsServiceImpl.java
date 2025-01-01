package com.instape.app.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.instape.app.cloudsql.model.Partner;
import com.instape.app.cloudsql.model.PartnerProductMapping;
import com.instape.app.cloudsql.model.ProductCatalog;
import com.instape.app.cloudsql.model.ProductDTO;
import com.instape.app.cloudsql.repository.PartnerProductsMappingRepository;
import com.instape.app.cloudsql.repository.PartnerRepository;
import com.instape.app.cloudsql.repository.ProductCatalogRepository;
import com.instape.app.request.PartnerProductsRequestPayload;
import com.instape.app.service.PartnerProductsService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Nov-2024
 * @ModifyDate - 07-Nov-2024
 * @Desc -
 */
@Service
public class PartnerProductsServiceImpl implements PartnerProductsService {

	static final Logger logger = LogManager.getLogger(PartnerProductsServiceImpl.class);

	@Autowired
	private PartnerProductsMappingRepository partnerProductsMappingRepository;

	@Autowired
	private PartnerRepository partnerRepository;

	@Autowired
	private ProductCatalogRepository productCatalogRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Map<Object, Object> create(String loggedInUserId, PartnerProductsRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Create Partner Products Mapping Service");
			Partner partnerExist = partnerRepository.findById(payload.getPartnerId()).orElse(null);
			if (partnerExist == null) {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("message", "Partner not found");
				stopWatch.stop();
				logger.info(String.format("Time taken on Partner Products Mapping Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Partner Products Mapping Service.");
				return responseMap;
			}

			List<Long> updateProductIds = payload.getProductIds();
			List<PartnerProductMapping> partnerProductMappings = partnerExist.getPartnerProductMapping();
			List<Long> existingProductIds = partnerProductMappings.stream().filter(ppm -> ppm.isDeleted() == false)
					.map(lpcm -> lpcm.getProductCatalog().getId()).collect(Collectors.toList());

			if (partnerProductMappings != null && !partnerProductMappings.isEmpty()) {
				partnerProductMappings = partnerProductMappings.stream().filter(ppm -> ppm.isDeleted() == false)
						.collect(Collectors.toList());
				for (PartnerProductMapping partnerProductMapping : partnerProductMappings) {
					if (!updateProductIds.contains(partnerProductMapping.getProductCatalog().getId())) {
						partnerProductMapping.setDeleted(true);
						partnerProductMapping.setUpdatedBy(Long.parseLong(loggedInUserId));
						partnerProductMapping.setUpdatedDate(DateUtils.getCurrentTimestamp());
					}
				}
			}

			for (Long productId : updateProductIds) {
				if (!existingProductIds.contains(productId)) {
					ProductCatalog productCatalog = productCatalogRepository.findById(productId).orElse(null);
					if (productCatalog != null) {
						PartnerProductMapping partnerProductMapping = new PartnerProductMapping();
						partnerProductMapping.setPartner(partnerExist);
						partnerProductMapping.setProductCatalog(productCatalog);
						partnerProductMapping.setDeleted(false);
						partnerProductMapping.setStatus(PortalConstant.ACTIVE);
						partnerProductMapping.setCreatedBy(Long.parseLong(loggedInUserId));
						partnerProductMapping.setCreatedDate(DateUtils.getCurrentTimestamp());
						partnerProductMapping.setUpdatedBy(Long.parseLong(loggedInUserId));
						partnerProductMapping.setUpdatedDate(DateUtils.getCurrentTimestamp());
						partnerProductMappings.add(partnerProductMapping);
					}
				}
			}

			partnerProductsMappingRepository.saveAll(partnerProductMappings);
			logger.info("Partner Product Catalog Mapped Successfully");

			responseMap.put("code", PortalConstant.OK);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			responseMap.put("message", "Partner Product Mapped Successfully");
			stopWatch.stop();
			logger.info(String.format("Time taken on Partner Products Mapping Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Partner Products Mapping Service.");
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Partner Products Mapping Service.", e);
			logger.info(String.format("Time taken on Partner Products Mapping Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Partner Products Mapping Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getById(Long id) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Get Partner Products Mapping Service");
			Partner partnerExist = partnerRepository.findById(id).orElse(null);
			if (partnerExist == null) {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("message", "Partner not found");
				stopWatch.stop();
				logger.info(String.format("Time taken on Get Partner Products Mapping Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner Products Mapping Service.");
				return responseMap;
			}

			List<ProductDTO> partnerProducts = null;
			List<PartnerProductMapping> partnerProductMappings = partnerExist.getPartnerProductMapping();
			if (partnerProductMappings != null && !partnerProductMappings.isEmpty()) {
				partnerProducts = partnerProductMappings.stream().filter(ppm -> ppm.isDeleted() == false)
						.map(ppm -> modelMapper.map(ppm.getProductCatalog(), ProductDTO.class))
						.collect(Collectors.toList());
			}
			logger.info("Get Partner Products Mapping Successfully");

			responseMap.put("code", PortalConstant.OK);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("data", partnerProducts);

			stopWatch.stop();
			logger.info(String.format("Time taken on Get Partner Products Mapping Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Products Mapping Service.");
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Partner Products Mapping Service.", e);
			logger.info(String.format("Time taken on Get Partner Products Mapping Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Products Mapping Service.");
			return responseMap;
		}
	}
}
