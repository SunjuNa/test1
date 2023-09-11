package com.megait.comicnovel.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.WebApplicationContext;

import com.megait.comicnovel.ComicnovelApplication;
import com.megait.comicnovel.bean.vo.GoogleResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes= {ComicnovelApplication.class})
@Slf4j
public class PeopleQuickstartTest {
	private MockMvc mockMvc;
	@Value("${google.client.id}")
	private String googleClientId;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	public void setUp() {
		//가짜 MVC에 WebApplicationContext를 전달한 후 환경 생성
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void google19YN(@ModelAttribute GoogleResponse googleresponse, Model model, HttpServletRequest httprequest, HttpServletResponse httpresponse) throws IOException, GeneralSecurityException {
		
	}
}
