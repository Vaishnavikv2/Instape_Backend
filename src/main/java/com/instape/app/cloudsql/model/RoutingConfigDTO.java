package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 11-Nov-2024
 * @ModifyDate - 11-Nov-2024
 * @Desc -
 */
public class RoutingConfigDTO {

	private String id;

	private String partnerId;

	private String productId;

	private String partnerServiceId;

	private String command;

	private String screen;

	private String category;

	private String topicId;

	private String keyword;

	private String authRequired;

	private String restEndpoint;

	private String status;

	private boolean isDeleted;

	private String userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPartnerServiceId() {
		return partnerServiceId;
	}

	public void setPartnerServiceId(String partnerServiceId) {
		this.partnerServiceId = partnerServiceId;
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
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
