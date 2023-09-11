package com.megait.comicnovel.dependency;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class DependencyTest {
	@Autowired
	private Coding coding;
	
	@Autowired 
	private Computer computer;
	
	@Test
	public void checkDI() {
		log.info("-----------------");
		log.info("coding: "+coding);
		log.info("computer: "+computer);
	}
}
