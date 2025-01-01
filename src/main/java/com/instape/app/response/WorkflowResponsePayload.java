package com.instape.app.response;

import java.util.List;
import com.instape.app.cloudsql.model.ProcessConfigDTO;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 22-Jul-2024
 * @ModifyDate - 22-Jul-2024
 * @Desc -
 */
public class WorkflowResponsePayload {

	private Long id;

	private String status;

	private String contractCode;

	private List<ProcessConfigDTO> data;

	private long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public List<ProcessConfigDTO> getData() {
		return data;
	}

	public void setData(List<ProcessConfigDTO> data) {
		this.data = data;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
