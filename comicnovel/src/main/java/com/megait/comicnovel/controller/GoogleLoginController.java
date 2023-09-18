package com.megait.comicnovel.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.megait.comicnovel.bean.vo.GoogleInfResponse;
import com.megait.comicnovel.bean.vo.GoogleRequest;
import com.megait.comicnovel.bean.vo.GoogleResponse;
import com.megait.comicnovel.bean.vo.UserVO;
import com.megait.comicnovel.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j 
public class GoogleLoginController {
	private final UserService service;
	
	@Value("${google.client.id}")
	private String googleClientId;
	@Value("${google.secret}")
	private String googleClientPw;
	
	@RequestMapping(value="/login/google", method={RequestMethod.GET})
	public void loginGoogle(HttpServletResponse response) throws IOException {
		log.info("--------------------");
		log.info("[GoogleLoginController] getGoogleAuthUrl()");
		log.info("--------------------");
		
		String reqUrl=
				"https://accounts.google.com/o/oauth2/v2/auth?client_id="
				+googleClientId
				+"&redirect_uri=http://localhost:10002/oauth_google"
				+ "&response_type=code"
				+ "&scope=email%20profile%20openid"
				;
		
		response.sendRedirect(reqUrl);
		
	}


	
	@RequestMapping(value="/oauth_google", method={RequestMethod.GET})
	public RedirectView loginGoogle(@RequestParam(value="code") String authCode, HttpSession session, Model model, HttpServletResponse response) throws IOException {
		//참고한 사이트
		//https://mjoo1106.tistory.com/entry/Spring-Google-Login-API-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
		log.info("--------------------");
		log.info("[GoogleLoginController] oauth_google()");
		log.info("--------------------");
		
		RestTemplate restTemplate = new RestTemplate();
		
		//google로그인 사용허가 인증
		GoogleRequest googleReqVarDTO = GoogleRequest.builder()
				.clientId(googleClientId)
				.redirectUri("http://localhost:10002/oauth_google")
				.clientSecret(googleClientPw)
				.code(authCode)
				.grantType("authorization_code")
				.build();
		
		log.info("DTO: "+ googleReqVarDTO.toString());
		
		//1.api를 호출하여 결과 가져오기(토큰 가져오기)
		//RestTemplate.getForObject(URI uri, Class<T> responseType) => (호출하는 url, 반환타입)
		ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity(
						"https://oauth2.googleapis.com/token", 
						googleReqVarDTO, 
						GoogleResponse.class);
		
		log.info("resultEntity는 "+resultEntity);
		
		//결과적으로 받아온 토큰
		String jwtToken = resultEntity.getBody().getId_token();
		
		//특수
//		GoogleOAuthToken gtoken = new GoogleOAuthToken(
//				resultEntity.getBody().getAccess_token(), 
//				resultEntity.getBody().getExpires_in(), 
//				resultEntity.getBody().getScope(),
//				resultEntity.getBody().getToken_type(),
//				resultEntity.getBody().getId_token());
//		//https://devgoat.tistory.com/31
//		ObjectMapper objectMapper = new ObjectMapper();
//		String strcookie = null;
//		strcookie = objectMapper.writeValueAsString(gtoken);
		
		Map<String, String> map = new HashMap<>();
		
		map.put("id_token", jwtToken);
		
		//토큰을 통해 받아올 개인정보
		ResponseEntity<GoogleInfResponse> resultEntity2 = restTemplate.postForEntity(
							"https://oauth2.googleapis.com/tokeninfo", 
							map, 
							GoogleInfResponse.class);
		
		String email = resultEntity2.getBody().getEmail();
		String name = resultEntity2.getBody().getName();
		
		System.out.println("email은 "+email);
		System.out.println(resultEntity2.getBody().toString());
		
		UserVO user = service.getUserByEmail(email);//db에서 가져옴
		
		if(user==null) {//회원가입이 안됨
			UserVO newbie = new UserVO();//db <-> googleInfResponse: api를 통해가져온 값
			newbie.setUserEmail(email);
			newbie.setNickname(name);
			service.insertUser(newbie);
			session.setAttribute("user", newbie);
			log.info("로그인이 되어있지 않아, 회원가입을 진행했고, 이메일은 다음과 같습니다" + email);
			return new RedirectView("comicnovel/home");
		}
		if(email!=null) {//로그인된 상태
			session.setAttribute("user", user);
			//https://baejangho.com/entry/Java-save-String-to-file
			//https://yangbox.tistory.com/63
//			String filePath = "C:\\Users\\sunjuNa\\Desktop\\SunjuNa\\computer\\04_back\\03_servlet\\comicnovel\\src\\main\\resources\\static\\token";
//			File file = new File(filePath);
//			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
//			writer.write(strcookie);
//			writer.close();
			log.info("이미 로그인이 되어있습니다 : " + user.getUserEmail());
			return new RedirectView("comicnovel/home");
		}
		return new RedirectView("comicnovel/loginFail");//로그인되지 않은 상태
	}
	
	//google에서 생일을 받아오는 건 내 스킬한계로 javascript로 받아와서 이모양 이꼬라지이다
	@RequestMapping(value="/CheckOK", method={RequestMethod.POST})
	public RedirectView checkOK(int birthday_year, String userEmail, HttpSession session) {
		log.info("[checkOK()] home test calling....");
		log.info("생년"+birthday_year);
		log.info("이메일은"+userEmail);
		int get_birthyear =birthday_year;
		LocalDate now = LocalDate.now();
		char adultYN;
		int now_year = now.getYear();
		if(get_birthyear>(now_year-20)) {
			adultYN = 'N';
			UserVO user = service.getUserByEmail(userEmail);
			if(user==null) {
				return new RedirectView("comicnovel/needJoin");
			}
			else {
				log.info(""+user);
				user.setAdultYN(adultYN);
				service.updateUser(user);
				session.setAttribute("user", user);
			}
		}else {
			adultYN='Y';
			UserVO user = service.getUserByEmail(userEmail);
			if(user==null) {
				return new RedirectView("comicnovel/needJoin");
			}
			else {
				log.info(""+user);
				user.setAdultYN(adultYN);
				service.updateUser(user);
				session.setAttribute("user", user);
			}
		}
		return new RedirectView("comicnovel/home");
	}
	
//	@ResponseBody
//	@RequestMapping(value="birthday_check", method={RequestMethod.POST})
//	public void birthday_check(HttpServletRequest request) {
//		System.out.println("태어난 해는"+request.getParameter("birthday_year"));
//		//UserVO user = service.getUserByEmail(email);
//	}
	
	//모델로 모든것을 해내려했을때
//	@RequestMapping(value="/oauth_google", method={RequestMethod.GET})
//	public RedirectView loginGoogle(@RequestParam(value="code") String authCode, Model model) {
//		//참고한 사이트
//		//https://mjoo1106.tistory.com/entry/Spring-Google-Login-API-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
//		log.info("--------------------");
//		log.info("[GoogleLoginController] oauth_google()");
//		log.info("--------------------");
//		
//		RestTemplate restTemplate = new RestTemplate();
//		
//		//google로그인 사용허가 인증
//		GoogleRequest googleReqVarDTO = GoogleRequest.builder()
//				.clientId(googleClientId)
//				.redirectUri("http://localhost:10002/oauth_google")
//				.clientSecret(googleClientPw)
//				.code(authCode)
//				.grantType("authorization_code")
//				.build();
//		
//		log.info("DTO: "+ googleReqVarDTO.toString());
//		
//		//1.api를 호출하여 결과 가져오기(토큰 가져오기)
//		//RestTemplate.getForObject(URI uri, Class<T> responseType) => (호출하는 url, 반환타입)
//		ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity(
//						"https://oauth2.googleapis.com/token", 
//						googleReqVarDTO, 
//						GoogleResponse.class);
//		
//		//결과적으로 받아온 토큰
//		String jwtToken = resultEntity.getBody().getId_token();
//		
//		//Person profile = ser
//		
//		Map<String, String> map = new HashMap<>();
//		
//		map.put("id_token", jwtToken);
//		
//		//토큰을 통해 받아올 개인정보
//		ResponseEntity<GoogleInfResponse> resultEntity2 = restTemplate.postForEntity(
//							"https://oauth2.googleapis.com/tokeninfo", 
//							map, 
//							GoogleInfResponse.class);
//		
//		String email = resultEntity2.getBody().getEmail();
//		String name = resultEntity2.getBody().getName();
//		
//		System.out.println("email은 "+email);
//		log.info("email은 "+email);
//		System.out.println(resultEntity2.getBody().toString());
//		
//		UserVO user = service.getUserByEmail(email);//db에서 가져옴
//		
//		if(user==null) {//회원가입이 안됨
//			UserVO newbie = new UserVO();
//			newbie.setUserEmail(email);
//			newbie.setNickname(name);
//			service.insertUser(newbie);
//			model.addAttribute("userVO", newbie);
//			log.info("로그인이 되어있지 않아, 회원가입을 진행했고, 이메일은 다음과 같습니다" + email);
//			return new RedirectView("comicnovel/home");
//		}
//		model.addAttribute("userEmail", email);//회원가입이 된 경우
//		if(email!=null) {//로그인된 상태
//			model.addAttribute("userVO", service.getUserByEmail(email));
//			log.info("이미 로그인이 되어있습니다: " + service.getUserByEmail(email));
//			return new RedirectView("comicnovel/home");
//		}
//		return new RedirectView("comicnovel/loginFail");//로그인되지 않은 상태
//		
//	}	

	//세션으로 만들었을때의 컨트롤러
//	@RequestMapping(value="/oauth_google", method={RequestMethod.GET})
//	public RedirectView loginGoogle(@RequestParam(value="code") String authCode, HttpSession session, Model model) {
//		//참고한 사이트
//		//https://mjoo1106.tistory.com/entry/Spring-Google-Login-API-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
//		log.info("--------------------");
//		log.info("[GoogleLoginController] oauth_google()");
//		log.info("--------------------");
//		
//		RestTemplate restTemplate = new RestTemplate();
//		
//		//google로그인 사용허가 인증
//		GoogleRequest googleReqVarDTO = GoogleRequest.builder()
//				.clientId(googleClientId)
//				.redirectUri("http://localhost:10002/oauth_google")
//				.clientSecret(googleClientPw)
//				.code(authCode)
//				.grantType("authorization_code")
//				.build();
//		
//		log.info("DTO: "+ googleReqVarDTO.toString());
//		
//		//1.api를 호출하여 결과 가져오기(토큰 가져오기)
//		//RestTemplate.getForObject(URI uri, Class<T> responseType) => (호출하는 url, 반환타입)
//		ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity(
//						"https://oauth2.googleapis.com/token", 
//						googleReqVarDTO, 
//						GoogleResponse.class);
//		
//		//결과적으로 받아온 토큰
//		String jwtToken = resultEntity.getBody().getId_token();
//		
//		//Person profile = ser
//		
//		Map<String, String> map = new HashMap<>();
//		
//		map.put("id_token", jwtToken);
//		
//		//토큰을 통해 받아올 개인정보
//		ResponseEntity<GoogleInfResponse> resultEntity2 = restTemplate.postForEntity(
//							"https://oauth2.googleapis.com/tokeninfo", 
//							map, 
//							GoogleInfResponse.class);
//		
//		String email = resultEntity2.getBody().getEmail();
//		String name = resultEntity2.getBody().getName();
//		
//		System.out.println("email은 "+email);
//		System.out.println(resultEntity2.getBody().toString());
//		
//		UserVO user = service.getUserByEmail(email);//db에서 가져옴
//		
//		if(user==null) {//회원가입이 안됨
//			UserVO newbie = new UserVO();
//			newbie.setUserEmail(email);
//			newbie.setNickname(name);
//			service.insertUser(newbie);
//			model.addAttribute("userVO", newbie);
//			log.info("로그인이 되어있지 않아, 회원가입을 진행했고, 이메일은 다음과 같습니다" + email);
//			return new RedirectView("comicnovel/home");
//		}
//		session.setAttribute("userEmail", email);
//		return new RedirectView("loginhome");
//		
//	}
	
//	@RequestMapping(value="/oauth_google", method={RequestMethod.GET})
//	public String loginGoogle(@RequestParam(value="code") String authCode) {
//		//참고한 사이트
//		//https://mjoo1106.tistory.com/entry/Spring-Google-Login-API-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
//		log.info("--------------------");
//		log.info("[GoogleLoginController] oauth_google()");
//		log.info("--------------------");
//		
//		RestTemplate restTemplate = new RestTemplate();
//		
//		//google로그인 사용허가 인증
//		GoogleRequest googleReqVarDTO = GoogleRequest.builder()
//				.clientId(googleClientId)
//				.redirectUri("http://localhost:10002/login/oauth_google")
//				.clientSecret(googleClientPw)
//				.code(authCode)
//				.grantType("authorization_code")
//				.build();
//		
//		log.info("DTO: "+ googleReqVarDTO.toString());
//		
//		//1.api를 호출하여 결과 가져오기(토큰 가져오기)
//		//RestTemplate.getForObject(URI uri, Class<T> responseType) => (호출하는 url, 반환타입)
//		ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity(
//						"https://oauth2.googleapis.com/token", 
//						googleReqVarDTO, 
//						GoogleResponse.class);
//		
//		//결과적으로 받아온 토큰
//		String jwtToken = resultEntity.getBody().getId_token();
//		
//		Map<String, String> map = new HashMap<>();
//		
//		map.put("id_token", jwtToken);
//		
//		//토큰을 통해 받아올 개인정보
//		ResponseEntity<GoogleInfResponse> resultEntity2 = restTemplate.postForEntity(
//							"https://oauth2.googleapis.com/tokeninfo", 
//							map, 
//							GoogleInfResponse.class);
//		
//		String email = resultEntity2.getBody().getEmail();
//		String name = resultEntity2.getBody().getName();
//		
//		System.out.println("email은 "+email);
//		System.out.println(resultEntity2.getBody().toString());
//		
//		return email;//이메일말고도 다른 정보도 가능
		
//	}
}
