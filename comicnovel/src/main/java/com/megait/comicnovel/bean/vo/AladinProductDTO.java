package com.megait.comicnovel.bean.vo;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AladinProductDTO {
	private String title;
	private String cover;
	private String author;
	private String pubDate;
	private int priceSales;
	private String categoryName;
	private String link;
	
	public AladinProductDTO(org.json.JSONObject aladinProductsJson) throws JSONException {
		this.title=aladinProductsJson.getString("title");
		this.cover=aladinProductsJson.getString("cover");
		this.author=aladinProductsJson.getString("author");
		this.pubDate=aladinProductsJson.getString("pubDate");
		this.priceSales = aladinProductsJson.getInt("priceSales");
		this.categoryName=aladinProductsJson.getString("categoryName");
		this.link = aladinProductsJson.getString("link");
		
	}
	
}
