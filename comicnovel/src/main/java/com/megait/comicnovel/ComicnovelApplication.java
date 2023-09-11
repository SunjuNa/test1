package com.megait.comicnovel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude= {SecurityAutoConfiguration.class})
public class ComicnovelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComicnovelApplication.class, args);
	}

}
