package com.megait.comicnovel.bean.vo;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Criteria {
	private int pageNum;//현재페이지
	private int amount;//한 페이지에 보여줄 게시글 개수
	private int maxResult; //몇개의 결과에 보여주기
	private int offset;//~까지 제끼고, 그 다음부터 나오기
	
	public Criteria() {
		this.pageNum=1;
		this.amount=10;
		setParam();
	}
	
	//오버로딩
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
		setParam();
	}
	
	public void setParam() {
		maxResult = amount;
		offset = (pageNum-1)*amount;
	}
	
}
