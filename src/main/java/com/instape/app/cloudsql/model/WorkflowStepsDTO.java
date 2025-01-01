package com.instape.app.cloudsql.model;

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
public class WorkflowStepsDTO {

	private String desc;

	private String name;

	private String status;

	private long id;

	private int seqId;

	private boolean deleted;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
