package com.instape.app.cloudsql.model;

import java.util.List;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 03-Sep-2024
 * @ModifyDate - 03-Sep-2024
 * @Desc -
 */
public class LenderDTO {

	private Long id;

	private String logoPath;

	private String name;

	private String code;

	private String cinNumber;

	private String lenderAddress;

	private String lenderEmail;

	private String lenderPhone;

	private String gstNumber;

	private String status;

	private String website;

	private List<Long> products;

	private List<ProductDetailDTO> productDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCinNumber() {
		return cinNumber;
	}

	public void setCinNumber(String cinNumber) {
		this.cinNumber = cinNumber;
	}

	public String getLenderAddress() {
		return lenderAddress;
	}

	public void setLenderAddress(String lenderAddress) {
		this.lenderAddress = lenderAddress;
	}

	public String getLenderEmail() {
		return lenderEmail;
	}

	public void setLenderEmail(String lenderEmail) {
		this.lenderEmail = lenderEmail;
	}

	public String getLenderPhone() {
		return lenderPhone;
	}

	public void setLenderPhone(String lenderPhone) {
		this.lenderPhone = lenderPhone;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<Long> getProducts() {
		return products;
	}

	public void setProducts(List<Long> products) {
		this.products = products;
	}

	public List<ProductDetailDTO> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetailDTO> productDetails) {
		this.productDetails = productDetails;
	}
}
