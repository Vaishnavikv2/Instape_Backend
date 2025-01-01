package com.instape.app.response;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 11-Nov-2024
 * @ModifyDate - 11-Nov-2024
 * @Desc -
 */
public class RoutingConfigResponsePayload {

	private String id;

	private String partnerCode;

	private String productCode;

	private String partnerServiceCode;

	private long partnerServiceId;

	private long productId;

	private long partnerId;

	private String command;

	private String screen;

	private String category;

	private String topicId;

	private String keyword;

	private String authRequired;

	private String restEndpoint;

	private String status;

	private boolean deleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPartnerServiceCode() {
		return partnerServiceCode;
	}

	public void setPartnerServiceCode(String partnerServiceCode) {
		this.partnerServiceCode = partnerServiceCode;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getAuthRequired() {
		return authRequired;
	}

	public void setAuthRequired(String authRequired) {
		this.authRequired = authRequired;
	}

	public String getRestEndpoint() {
		return restEndpoint;
	}

	public void setRestEndpoint(String restEndpoint) {
		this.restEndpoint = restEndpoint;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public long getPartnerServiceId() {
		return partnerServiceId;
	}

	public void setPartnerServiceId(long partnerServiceId) {
		this.partnerServiceId = partnerServiceId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}
}
