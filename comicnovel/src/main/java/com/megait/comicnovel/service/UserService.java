package com.megait.comicnovel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.megait.comicnovel.bean.vo.UserVO;

@Service
public interface UserService {
	public List<UserVO> getList();
	public UserVO getUserByEmail(String userEmail);
	public void insertUser(UserVO userVO);
	public void insertCheck19(char adultYN);
	public void deleteUser(int uno);
	public void updateUser(UserVO userVO);
}

