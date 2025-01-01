package com.instape.app.cloudsql.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractProductDTO {

	private Long id;
	private String contractCode;

	private Long productId;

	private String productName;
	
	private String status;

	public ContractProductDTO() {
		super();
	}

	public ContractProductDTO(Long id, Long productId, String productName,String status) {
		super();
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.status=status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}