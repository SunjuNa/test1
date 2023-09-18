package com.megait.comicnovel.bean.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AladinNewBookReqDTO {
	String ttbkey;
	String QueryType;
	Integer MaxResults;
	Integer start;
	String SearchTarget;
	String output;
	String Cover;
	Integer Version;
	Integer CategoryId;
}
