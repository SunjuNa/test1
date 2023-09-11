package com.megait.comicnovel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.megait.comicnovel.bean.vo.UserVO;

@Mapper
public interface UserMapper {
	public List<UserVO> getList();
	public UserVO getUserByEmail(String UserEmail);
	public void insertUser(UserVO userVo);
	public void insertCheck19(char adultYN);
	public void delete(int uno);
	public void update(UserVO userVO);
}
