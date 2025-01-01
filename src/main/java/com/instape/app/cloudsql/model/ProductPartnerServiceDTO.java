package com.instape.app.cloudsql.model;

import java.util.List;

public class ProductPartnerServiceDTO {
	private Long id;

	private String partnerName;

	private String code;
	
	List<PartnerServiceDTO> services;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<PartnerServiceDTO> getServices() {
		return services;
	}

	public void setServices(List<PartnerServiceDTO> services) {
		this.services = services;
	}	
	
}
