package com.megait.comicnovel.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class UserMapperTest {
	@Autowired
	private UserMapper mapper;
	
	@Test
	public void testGetList() {
		mapper.getList().forEach(member -> log.info(member.toString()));
	}
}
