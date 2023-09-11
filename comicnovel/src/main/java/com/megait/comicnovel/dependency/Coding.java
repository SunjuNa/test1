package com.megait.comicnovel.dependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class Coding {
	@Autowired
	private Computer myComputer;
}
