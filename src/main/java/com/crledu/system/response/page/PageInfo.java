package com.crledu.system.response.page;

public class PageInfo {

	private Integer pageSize;

	private Integer pageNum;

	public PageInfo() {
		super();
	}
	
	
	public PageInfo(Integer pageSize, Integer pageNum) {
		super();
		this.pageSize = pageSize;
		this.pageNum = pageNum;
	}



	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	
	
}
