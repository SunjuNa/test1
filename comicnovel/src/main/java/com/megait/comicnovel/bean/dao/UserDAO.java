package com.megait.comicnovel.bean.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.megait.comicnovel.bean.vo.UserVO;
import com.megait.comicnovel.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDAO {
	@Autowired
	private UserMapper mapper;
	
	public List<UserVO> getList(){
		return mapper.getList();
	}
	
	public UserVO getUserByEmail(String userEmail) {
		return mapper.getUserByEmail(userEmail);
	}
	
	public void insertUser(UserVO userVo) {
		mapper.insertUser(userVo);
	}
	
	public void insertCheck19(char adultYN) {
		mapper.insertCheck19(adultYN);
	}
	
	public void delete(int uno) {
		mapper.delete(uno);
	}
	public void update(UserVO userVO) {
		mapper.update(userVO);
	}
}
