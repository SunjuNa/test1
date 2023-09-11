package com.megait.comicnovel.bean.vo;

import org.springframework.boot.configurationprocessor.json.JSONException;

import lombok.Getter;

@Getter
public class GoogleProductDTO {
	private int year;
	private int month;
	private int day;
	
	public GoogleProductDTO(org.json.JSONObject googleProductsJson) throws JSONException{
		this.year=googleProductsJson.getInt("year");
		this.month=googleProductsJson.getInt("month");
		this.day=googleProductsJson.getInt("day");
	}

}
