package com.megait.comicnovel.bean.vo;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UserVO {
	private int uno;
	private String userEmail;
	private String nickname;
	private char adultYN;
	private String regdate;
	private String updatedate;
}
