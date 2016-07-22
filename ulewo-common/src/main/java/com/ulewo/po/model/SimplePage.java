package com.ulewo.po.model;

public class SimplePage {

	private int pageNo;
	private int count;
	private int pageSize;
	
	public SimplePage(int pageNo, int count, int pageSize){
		this.pageNo = pageNo;
		this.count=count;
		this.pageSize=pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
