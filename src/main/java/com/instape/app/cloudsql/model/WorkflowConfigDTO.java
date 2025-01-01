package com.instape.app.cloudsql.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 22-Jul-2024
 * @ModifyDate - 22-Jul-2024
 * @Desc -
 */
@JsonSerialize
public class WorkflowConfigDTO {

	private String desc;

	private String workflowId;

	private String status;

	private long id;

	private int seqId;

	private boolean deleted;

	private List<WorkflowStepsDTO> steps;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public List<WorkflowStepsDTO> getSteps() {
		return steps;
	}

	public void setSteps(List<WorkflowStepsDTO> steps) {
		this.steps = steps;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
