package com.megait.comicnovel.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper {
	public void insertTest1(String id, String name);
	public void insertTest2(String name);
}
