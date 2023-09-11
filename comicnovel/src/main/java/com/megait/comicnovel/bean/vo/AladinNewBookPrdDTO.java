package com.megait.comicnovel.bean.vo;

import org.json.JSONException;

import lombok.Getter;

@Getter
public class AladinNewBookPrdDTO {
	private String title;
	private String link;
	private String author;
	private String cover;
	private boolean adult;
	private Integer customerReviewRank;
	
	public AladinNewBookPrdDTO(org.json.JSONObject aladinNewBookPrdJSON) throws JSONException{
		this.title=aladinNewBookPrdJSON.getString("title");
		this.link=aladinNewBookPrdJSON.getString("link");
		this.author=aladinNewBookPrdJSON.getString("author");
		this.cover=aladinNewBookPrdJSON.getString("cover");
		this.adult=aladinNewBookPrdJSON.getBoolean("adult");
		this.customerReviewRank=aladinNewBookPrdJSON.getInt("customerReviewRank");
		
	}
}
