package com.instape.app.request;

import java.util.List;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 01-Aug-2024
 * @ModifyDate - 01-Aug-2024
 * @Desc -
 */
public class DownloadFilesRequestPayload {

	private List<Long> data;

	public List<Long> getData() {
		return data;
	}

	public void setData(List<Long> data) {
		this.data = data;
	}
}
