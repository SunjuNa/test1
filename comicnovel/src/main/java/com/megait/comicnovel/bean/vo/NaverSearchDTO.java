package com.megait.comicnovel.bean.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NaverSearchDTO {
	String query;
	Integer display;
	Integer start;
	
}
