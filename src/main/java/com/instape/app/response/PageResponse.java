package com.instape.app.response;

public class PageResponse {
	private Long totalcount;

	private int pageNumber;

	private int numberOfElements;

	private int size;
	
	public PageResponse() {
		super();
	}

	public PageResponse(Long totalcount, int pageNumber, int numberOfElements, int size) {
		super();
		this.totalcount = totalcount;
		this.pageNumber = pageNumber;
		this.numberOfElements = numberOfElements;
		this.size = size;
	}

	public Long getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(Long totalcount) {
		this.totalcount = totalcount;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
