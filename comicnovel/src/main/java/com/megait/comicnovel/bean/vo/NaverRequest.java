package com.megait.comicnovel.bean.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NaverRequest {
	private String grant_type;
	private String client_id;
	private String client_secret;
	private String code;
	private String state;
//	private String refresh_token;
//	private String access_token;
//	private String service_provider;
	
}
