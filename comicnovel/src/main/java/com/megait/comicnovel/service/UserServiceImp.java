package com.megait.comicnovel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.megait.comicnovel.bean.dao.UserDAO;
import com.megait.comicnovel.bean.vo.UserVO;

@Service
public class UserServiceImp implements UserService{
	@Autowired
	private UserDAO userDAO;

	@Override
	public List<UserVO> getList() {
		// TODO Auto-generated method stub
		return userDAO.getList();
	}

	@Override
	public UserVO getUserByEmail(String userEmail) {
		// TODO Auto-generated method stub
		return userDAO.getUserByEmail(userEmail);
	}

	@Override
	public void insertUser(UserVO userVO) {
		// TODO Auto-generated method stub
		userDAO.insertUser(userVO);
	}

	@Override
	public void insertCheck19(char adultYN) {
		// TODO Auto-generated method stub
		userDAO.insertCheck19(adultYN);
	}

	@Override
	public void deleteUser(int uno) {
		// TODO Auto-generated method stub
		userDAO.delete(uno);
	}

	@Override
	public void updateUser(UserVO userVO) {
		// TODO Auto-generated method stub
		userDAO.update(userVO);
	}
	
	
	
	
}
