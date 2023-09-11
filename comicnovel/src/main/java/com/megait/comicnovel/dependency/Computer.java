package com.megait.comicnovel.dependency;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Computer {
	private String name = "My super Com";
	String cpu = "None";
	String memory = "1M";
}
