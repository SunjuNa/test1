package com.megait.comicnovel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfiguration implements WebMvcConfigurer{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry reg) {
		// TODO Auto-generated method stub
		reg.addResourceHandler("/js/**")  //js안에 있는 것은 
		   .addResourceLocations("classpath:/static/js/") //사실 여기에 있는 것들이고
		   .setCachePeriod(60*60*24*365); //1년내내 초마다 갱신하기
		reg.addResourceHandler("/css/**")
		   .addResourceLocations("classpath:/static/css/")
		   .setCachePeriod(60*60*24*365);
		reg.addResourceHandler("/img/**")
		   .addResourceLocations("classpath:/static/img/")
		   .setCachePeriod(60*60*24*365);
		reg.addResourceHandler("/font/**")
		   .addResourceLocations("classpath:/static/font/")
		   .setCachePeriod(60*60*24*365);
	
}
}