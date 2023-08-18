package com.megait.comicnovel.bean.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AladinReqSearchVarDTO {
	private String ttbkey;
	String query;
	private Integer start;
	private Integer maxResult;
}
