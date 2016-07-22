package com.ulewo.utils;

import java.util.List;

import com.ulewo.po.model.SimplePage;


//从2.0代码中复制的，不对
//public class PaginationResult {

public class PaginationResult<T> {

	private List<?> list;

	private SimplePage page;

	public PaginationResult(SimplePage page, List<?> list) {
		this.list = list;
		this.page = page;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public SimplePage getPage() {
		return page;
	}

	public void setPage(SimplePage page) {
		this.page = page;
	}
	
}
