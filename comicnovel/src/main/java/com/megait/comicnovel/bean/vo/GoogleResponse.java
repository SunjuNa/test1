package com.megait.comicnovel.bean.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleResponse {
	private String access_token;
	private int expires_in;
	private String refresh_token;
	private String scope;
	private String token_type;
	private String id_token;
}
