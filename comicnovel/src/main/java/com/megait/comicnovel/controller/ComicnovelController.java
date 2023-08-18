package com.megait.comicnovel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comicnovel/*")
@Slf4j 
public class ComicnovelController {
	
	@RequestMapping(value="/home", method= {RequestMethod.GET})
	public void home() {
		log.info("[home()] home test calling....");
	}
	
	@GetMapping("/01_test")
	public void test1() {
		log.info("[test01()] test01 calling....");
	}
	
	@GetMapping("/Login")
	public void Login() {
		log.info("[Login()] login test calling...");
	}
	
	 @GetMapping("/SearchResult") 
	 public void showResult(Model model) {
		 log.info("--------------------");
		 log.info("[comicNovelController] showResult()");
		 log.info("--------------------");
			/* model.addAttribute("results", results); */
	  }
	 
}
