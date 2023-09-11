package com.megait.comicnovel.controller;

import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.megait.comicnovel.bean.vo.AladinNewBookReqDTO;
import com.megait.comicnovel.bean.vo.AladinProductDTO;
import com.megait.comicnovel.bean.vo.AladinReqVarDTO;
import com.megait.comicnovel.bean.vo.UserVO;
import com.megait.comicnovel.service.BookSearchService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comicnovel/*")
@Slf4j 
public class ComicnovelController {
	private final BookSearchService bookSearchService;
			
	@RequestMapping(value="/home", method= {RequestMethod.GET})
	public void home(HttpSession session, Model model) {
		log.info("[home()] home test calling....");
		log.info(""+ session.getAttribute("user"));
//		log.info(""+ session.getAttribute("adultYN"));
		if(session!=null) {
			//model -> view부분에 값을 전달해줌
			//session -> 가지고 있는 정보, view로 가져올수있는지는 미지수
			UserVO specificuser = (UserVO) session.getAttribute("user");//session에서 object로서 값을 가져오려면 casting을 해줘야한다.
			model.addAttribute("userVO", specificuser);
		}
	}
	
	@GetMapping("01_test")
	public void test1() {
		log.info("[test01()] test01 calling....");
	}
	
	@GetMapping("02_test")
	public void test2() {
		log.info("[test02()] test01 calling....");
	}
	
	@GetMapping("needJoin")
	public void needJoin() {
		log.info("[needJoin()] test01 calling....");
	}
	
	@GetMapping("Login")
	public void Login() {
		log.info("google,naver,kakao login access");
	}
	
	@GetMapping("MyPage")
	public void myPage() {
		log.info("[myPage()] calling...");
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
	
	@GetMapping("Verify19")
	public void verify19(){
		log.info("[verify19OK()] verify19OK() calling....");
	}
	
	@GetMapping("newBook")
	public void newBookResult(Model model, String query) {
		 log.info("--------------------");
		 log.info("[comicNovelController] newBookResult()");
		 log.info("--------------------");
		 
		 AladinNewBookReqDTO newBookreqDTO = AladinNewBookReqDTO.builder()
				 	.ttbkey("ttbnasnju1839001")
					.QueryType("ItemNewAll")
					.MaxResults(10)
					.start(1)
					.SearchTarget("Book")
					.output("XML")
					.Version(20131101)
					.build();
	}
}
