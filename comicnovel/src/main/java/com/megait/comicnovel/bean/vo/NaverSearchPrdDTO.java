package com.megait.comicnovel.bean.vo;

import org.json.JSONObject;

import lombok.Getter;

@Getter
public class NaverSearchPrdDTO {
	private String title;
	private String link;
	private String description;
	private String bloggername;
	
	public NaverSearchPrdDTO(JSONObject itemJson) {
		this.title=itemJson.getString("title");
		this.link=itemJson.getString("link");
		this.description=itemJson.getString("description");
		this.bloggername=itemJson.getString("bloggername");
	}
}
