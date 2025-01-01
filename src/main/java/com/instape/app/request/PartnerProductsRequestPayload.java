package com.instape.app.request;

import java.util.List;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Nov-2024
 * @ModifyDate - 07-Nov-2024
 * @Desc -
 */
public class PartnerProductsRequestPayload {

	private Long partnerId;

	private List<Long> productIds;

	public PartnerProductsRequestPayload(Long partnerId, List<Long> productIds) {
		super();
		this.partnerId = partnerId;
		this.productIds = productIds;
	}

	public PartnerProductsRequestPayload() {
		super();
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public List<Long> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<Long> productIds) {
		this.productIds = productIds;
	}
}
