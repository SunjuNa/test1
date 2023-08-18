package com.megait.comicnovel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.megait.comicnovel.bean.dao.AladinReqSearchVarDAO;
import com.megait.comicnovel.bean.vo.Criteria;

@Mapper
public interface AladinReqSearchVarMapper {
	//검색책 전체보기
	public List<AladinReqSearchVarDAO> getList();
}
