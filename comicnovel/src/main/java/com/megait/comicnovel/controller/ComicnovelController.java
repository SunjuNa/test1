package com.megait.comicnovel.controller;

import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.megait.comicnovel.bean.vo.AladinNewBookPrdDTO;
import com.megait.comicnovel.bean.vo.AladinNewBookReqDTO;
import com.megait.comicnovel.bean.vo.AladinProductDTO;
import com.megait.comicnovel.bean.vo.AladinReqVarDTO;
import com.megait.comicnovel.bean.vo.NaverSearchDTO;
import com.megait.comicnovel.bean.vo.NaverSearchPrdDTO;
import com.megait.comicnovel.bean.vo.UserVO;
import com.megait.comicnovel.service.BookSearchService;
import com.megait.comicnovel.service.NaverSearchService;
import com.megait.comicnovel.service.NewBookService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comicnovel/*")
@Slf4j 
public class ComicnovelController {
	private final BookSearchService bookSearchService;
	private final NewBookService newbookService;
	private final NaverSearchService naverSearchService;
			
	@RequestMapping(value="/home", method= {RequestMethod.GET})
	public void home(HttpSession session, Model model) {
		log.info("[home()] home test calling....");
		log.info(""+ session.getAttribute("user"));
//		log.info(""+ session.getAttribute("adultYN"));
		
		//로그인
		if(session!=null) {
			//model -> view부분에 값을 전달해줌
			//session -> 가지고 있는 정보, view로 가져올수있는지는 미지수
			UserVO specificuser = (UserVO) session.getAttribute("user");//session에서 object로서 값을 가져오려면 casting을 해줘야한다.
			model.addAttribute("userVO", specificuser);
		}
		
		//알라딘 신작리스트 
		log.info("newBook collection is here");
		AladinNewBookReqDTO newBookreqDTO = AladinNewBookReqDTO.builder()
			 	.ttbkey("")
				.QueryType("ItemNewAll")
				.MaxResults(20)
				.start(1)
				.SearchTarget("Book")
				.output("xml")
				.Version(20131101)
				.CategoryId(2551)
				.build();
		
		//알라딘 상품 리스트
		List<AladinNewBookPrdDTO> results = newbookService.aladinNewBookAPI(newBookreqDTO);
		 for(int i=0; i<results.size(); i++) {
			 System.out.println("결과입니다--------------------------------------");
			 System.out.println(results.get(i).getTitle());
			 System.out.println(results.get(i).getAuthor());
			 System.out.println("성인물입니까?"+results.get(i).isAdult());
		 }
		 model.addAttribute("newbookRes", results);
		 
		 AladinNewBookReqDTO editorreqDTO = AladinNewBookReqDTO.builder()
				 	.ttbkey("")
					.QueryType("Bestseller")
					.MaxResults(20)
					.start(1)
					.Cover("Mid")
					.SearchTarget("Book")
					.output("xml")
					.Version(20131101)
					.CategoryId(2551)
					.build();
		 
		 List<AladinNewBookPrdDTO> editorresults = newbookService.aladinNewBookAPI(editorreqDTO);
		 for(int i=0; i<editorresults.size(); i++) {
			 System.out.println("결과입니다--------------------------------------");
			 System.out.println(editorresults.get(i).getTitle());
			 System.out.println("성인물입니까?"+ editorresults.get(i).isAdult());
		 }
		 
		 model.addAttribute("editorRes", editorresults);
		 //베너용으로 하나 추가
		 AladinNewBookReqDTO coverimagereqDTO2 = AladinNewBookReqDTO.builder()
				 	.ttbkey("")
					.QueryType("Bestseller")
					.MaxResults(20)
					.start(1)
					.Cover("Big")
					.SearchTarget("Book")
					.output("xml")
					.Version(20131101)
					.CategoryId(2551)
					.build();
		 System.out.println("베너용 커버 이미지입니다");
		 List<AladinNewBookPrdDTO> coverimageresults = newbookService.aladinNewBookAPI(coverimagereqDTO2);
		 String coverimage1 = coverimageresults.get(0).getCover();
		 String coverimage2 = coverimageresults.get(1).getCover();
		 String coverimage3 = coverimageresults.get(2).getCover();
		 System.out.println(""+coverimage3);
		 model.addAttribute("coverimage1", coverimage1);
		 model.addAttribute("coverimage2", coverimage2);
		 model.addAttribute("coverimage3", coverimage3);
		 
		 //네이버 블로그 검색
		 NaverSearchDTO naverReqDTO1 = NaverSearchDTO.builder()
				 .query(editorresults.get(0).getTitle())
				 .display(10)
				 .start(1)
				 .build();
		 
		 model.addAttribute("bestart", editorresults.get(0).getTitle());
		 
		 List<NaverSearchPrdDTO> naverProductDTOs1 = naverSearchService.naverSearchAPI(naverReqDTO1);
		 System.out.println("결과입니다---------------");
		 System.out.println(naverProductDTOs1.get(1).getLink());
		 
		 model.addAttribute("naverBlogList1", naverProductDTOs1);
		 
				 
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
		 
		 //알라딘 상품 검색 기능
		 AladinReqVarDTO aladinReqVarDTO = AladinReqVarDTO.builder()
				 	.ttbkey("")
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
		 model.addAttribute("query", query);	 
	  }
	
	@GetMapping("Verify19")
	public void verify19(){
		log.info("[verify19OK()] verify19OK() calling....");
	}
	
	@GetMapping("loginFail")
	public void loginFail() {
		log.info("[loginfail()] loginfail calling....");
	}
	
//	@GetMapping("newBook")
//	public void newBookResult(Model model) {
//		 log.info("--------------------");
//		 log.info("[comicNovelController] newBookResult()");
//		 log.info("--------------------");
//		 
//		 AladinNewBookReqDTO newBookreqDTO = AladinNewBookReqDTO.builder()
//				 	.ttbkey("")
//					.QueryType("ItemNewAll")
//					.MaxResults(10)
//					.start(1)
//					.SearchTarget("Book")
//					.output("XML")
//					.Version(20131101)
//					.build();
//		 
//		 List<AladinNewBookPrdDTO> results = newbookService.aladinNewBookAPI(newBookreqDTO);
//		 for(int i=0; i<results.size(); i++) {
//			 System.out.println("결과입니다--------------------------------------");
//			 System.out.println(results.get(i).getTitle());
//			 System.out.println(results.get(i).getAuthor());
//		 }
//		 model.addAttribute("results", results);
//	}
}
