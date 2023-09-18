package com.megait.comicnovel.bean.vo;

import org.json.JSONException;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class NaverInfResponse {
	private String id;
	private String nickname;
	private String email;
	private String name;
	private String gender;
	private String age;
	private String birthday;
	private String profile_image;
	private String birthyear;
	private String mobile;
	
	public NaverInfResponse(org.json.JSONObject naverProductsJson)throws JSONException{
		this.id=naverProductsJson.getString("id");
		this.nickname=naverProductsJson.getString("nickname");
		this.email=naverProductsJson.getString("email");
		this.name=naverProductsJson.getString("name");
		this.gender=naverProductsJson.getString("gender");
		this.age=naverProductsJson.getString("age");
		this.birthday=naverProductsJson.getString("birthday");
		this.profile_image=naverProductsJson.getString("profile_image");
		this.birthyear=naverProductsJson.getString("birthyear");
		this.mobile=naverProductsJson.getString("mobile");
	}
}
