package com.megait.comicnovel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication //(exclude= {DataSourceAutoConfiguration.class})
public class ComicnovelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComicnovelApplication.class, args);
	}

}
