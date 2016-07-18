package com.ulewo.mapper;

import java.util.List;

public interface BaseMapper<T,Q> {

	public void insert(T t);
	
	public void update(T t);
	
	public List<T> selectList(Q q);
	
	public Integer selectCount(Q q);
	
}
