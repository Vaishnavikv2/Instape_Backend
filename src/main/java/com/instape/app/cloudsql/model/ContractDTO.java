package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 15-Feb-2024
 * @ModifyDate - 15-Feb-2024
 * @Desc -
 */
public class ContractDTO {

	private String contractCode;

	private String contractName;

	private String contractId;

	public ContractDTO() {
		super();
	}

	public ContractDTO(String contractCode, String contractName) {
		super();
		this.contractCode = contractCode;
		this.contractName = contractName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
}
