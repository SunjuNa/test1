package com.megait.comicnovel.bean.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NaverResponse {
	private String access_token;
	private String refresh_token;
	private String token_type;
	private Integer expires_in;
	private String error;
	private String error_description;
}
