package com.instape.app.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.cloudsql.model.ProductDTO;
import com.instape.app.cloudsql.model.ProductPartnerServiceDTO;
import com.instape.app.cloudsql.model.ProductListDTO;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.response.PageableResponseDTO;
import com.instape.app.response.ResponseDTO;
import com.instape.app.service.ProductService;
import com.instape.app.utils.StatusCode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/product")
public class ProductController {
	private final Logger logger = LogManager.getLogger(ProductController.class);
	@Autowired
	private ProductService productService;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('PRODUCT.MAIN.READ')")
	public ResponseEntity<PageableResponseDTO> getAll(@RequestParam(defaultValue = "0") final Integer pageNumber,
            @RequestParam(defaultValue = "10") final Integer size,@RequestParam(defaultValue = "") final String text) {
		logger.info("Get all product request received");
		PageableResponseDTO productList = productService.getAll(text,PageRequest.of(pageNumber, size));
		logger.info("toatl product fetched:{}", productList.getPagination().getTotalcount());
		return new ResponseEntity<PageableResponseDTO>(productList, HttpStatus.OK);
	}
	
	@GetMapping("/list")
	@PreAuthorize("hasAnyAuthority('PRODUCT.MAIN.READ')")
	public ResponseEntity<List<ProductListDTO>> getAll() {
		logger.info("Get all product request received");
		List<ProductListDTO> productList = productService.getAll();
		logger.info("toatl product fetched:{}", productList.size());
		return new ResponseEntity<List<ProductListDTO>>(productList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('PRODUCT.MAIN.READ')")
	public ResponseEntity<ProductDTO> getById(@PathVariable Long id, HttpServletRequest request) {
		logger.info("Get product request received, productId:{}", id);
		ProductDTO productDto = productService.getById(id);
		return new ResponseEntity<ProductDTO>(productDto, HttpStatus.OK);
	}
	
	@GetMapping("/{productId}/partnerList")
	@PreAuthorize("hasAnyAuthority('PRODUCT.MAIN.READ')")
	public ResponseEntity<List<ProductPartnerServiceDTO>> getProductPartner(@PathVariable Long productId) {
		logger.info("Get product Partner request received, productId:{}", productId);
		List<ProductPartnerServiceDTO> partnerServiceList = productService.getProductPartnerServices(productId);
		return new ResponseEntity<List<ProductPartnerServiceDTO>>(partnerServiceList, HttpStatus.OK);
	}
	
	@GetMapping("/{contractproductId}/partner")
	@PreAuthorize("hasAnyAuthority('PRODUCT.MAIN.READ')")
	public ResponseEntity<List<ProductPartnerServiceDTO>> getContractProductPartner(@PathVariable Long contractproductId) {
		logger.info("Get contract product Partner request received, productId:{}", contractproductId);
		List<ProductPartnerServiceDTO> partnerServiceList = productService.getContractProductPartnerServices(contractproductId);
		return new ResponseEntity<List<ProductPartnerServiceDTO>>(partnerServiceList, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('PRODUCT.MAIN.CREATE')")
	public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO product, Authentication auth) {
		logger.info("Create product request received, product name:{}", product.getName());
		validatePayload(product);
		if (productService.isExistWithSameName(product.getName())) {
			throw new InstapeException("Product with same name already exist", HttpStatus.BAD_REQUEST.value());
		}
		Long loggedInUserId = Long.parseLong(auth.getName());
		ProductDTO productDto = productService.create(loggedInUserId, product);
		return new ResponseEntity<ProductDTO>(productDto, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('PRODUCT.MAIN.UPDATE')")
	public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO product, Authentication auth) {
		logger.info("Update product request received, product name:{}", product.getName());
		Long loggedInUserId = Long.parseLong(auth.getName());
		ProductDTO productDto = productService.update(loggedInUserId, product);
		return new ResponseEntity<ProductDTO>(productDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('PRODUCT.MAIN.DELETE')")
	public ResponseEntity<ResponseDTO> delete(@PathVariable Long id, Authentication auth) {
		logger.info("Delete product request received, productId:{}", id);
		Long loggedInUserId = Long.parseLong(auth.getName());
		productService.delete(loggedInUserId, id);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("Product has been deleted",StatusCode.OK,null), HttpStatus.OK);
	}

	private void validatePayload(ProductDTO product) {
		if (!StringUtils.hasText(product.getName())) {
			throw new InstapeException("Product name is mandatory", HttpStatus.BAD_REQUEST.value());
		}
	}
}
