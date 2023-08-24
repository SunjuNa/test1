package com.megait.comicnovel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.megait.comicnovel.bean.vo.AladinProductDTO;
import com.megait.comicnovel.bean.vo.AladinReqVarDTO;
import com.megait.comicnovel.service.BookSearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comicnovel/*")
@Slf4j 
public class ComicnovelController {
	private final BookSearchService bookSearchService;
	
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
	
	
	@GetMapping("searchResult")
	public void showResult(Model model, String query) throws JSONException{
		 log.info("--------------------");
		 log.info("[comicNovelController] showResult()");
		 log.info("--------------------");
		 log.info(query);
		 
		 AladinReqVarDTO aladinReqVarDTO = AladinReqVarDTO.builder()
				 	.ttbkey("ttbnasnju1839001")
					.Query(query) //input name이 query여야 query가 들어간다
					.QueryType("Keyword")
					.MaxResults(10)
					.start(1)
					.SearchTarget("Book")
					.output("XML")
					.Version(20131101)
					.build();
		 
		 List<AladinProductDTO> results = bookSearchService.aladinShopSearchAPI(aladinReqVarDTO);
		 for(int i=0; i<results.size(); i++) {
			 System.out.println("결과입니다--------------------------------------");
			 System.out.println(results.get(i).getTitle());
			 System.out.println(results.get(i).getCover());
		 }
		 model.addAttribute("results", results);
		 
	  }
	
}
