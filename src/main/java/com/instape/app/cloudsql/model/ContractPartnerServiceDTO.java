package com.instape.app.cloudsql.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractPartnerServiceDTO {

	private Long id;
	
	private Long partnerId;
	
	private String partnerName;
	
	@JsonProperty("ps_id")
	private Long partnerServiceId;
	
	@JsonProperty("ps_name")
	private String partnerServiceName;
	
	private String status;

	public ContractPartnerServiceDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Long getPartnerServiceId() {
		return partnerServiceId;
	}

	public void setPartnerServiceId(Long partnerServiceId) {
		this.partnerServiceId = partnerServiceId;
	}

	public String getPartnerServiceName() {
		return partnerServiceName;
	}

	public void setPartnerServiceName(String partnerServiceName) {
		this.partnerServiceName = partnerServiceName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
