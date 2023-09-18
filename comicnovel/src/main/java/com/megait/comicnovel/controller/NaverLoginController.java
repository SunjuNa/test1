package com.megait.comicnovel.controller;


import java.io.IOException;
import java.time.LocalDate;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.megait.comicnovel.bean.vo.NaverInfResponse;
import com.megait.comicnovel.bean.vo.UserVO;
import com.megait.comicnovel.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j 
public class NaverLoginController {
	private final UserService service;
	
	@Value("${naver.client.id}")
	private String naverClientId;
	@Value("${naver.secret}")
	private String naverClientPw;
	
	@RequestMapping(value="/login/naver", method={RequestMethod.GET})
	public void loginNaver(HttpServletResponse response) throws IOException {
		log.info("--------------------");
		log.info("[NaverLoginController] getNaverAuthUrl()");
		log.info("--------------------");
		
			String reqUrl=
			"https://nid.naver.com/oauth2.0/authorize?"
						+"response_type=code"
						+"&client_id=" + naverClientId
						+"&state=state"
						+"&redirect_uri="+"http://localhost:10002/oauth_naver"
				;
		response.sendRedirect(reqUrl);//naver의 경우 자동로그인이 되어있으면 popup창이 뜨지 않는다
}
	@RequestMapping(value="/oauth_naver", method={RequestMethod.GET})
	public RedirectView getNaverTokenUrl(@RequestParam(value="code") String authCode,
			@RequestParam(value="state") String state,
			HttpSession session) 
			throws Exception {
		log.info("--------------------");
		log.info("[NaverLoginController] oauth_naver()");
		log.info("--------------------");
		
		//Http header 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		//Http Body 생성
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("code", authCode);
		requestBody.add("state", state);
		requestBody.add("client_id", naverClientId);
		requestBody.add("client_secret", naverClientPw);
		requestBody.add("grant_type", "authorization_code");
		
		//https://young-bin.tistory.com/91
		//https://dazbee.tistory.com/86
		
		//Http 요청 보내기
		HttpEntity<MultiValueMap<String,String>> naverTokenRequest = 
				new HttpEntity<>(requestBody, headers);
//		ResponseEntity<?> resultEntity =  new RestTemplate().postForEntity("https://nid.naver.com/oauth2.0/token", requestBody,Map.class);
		RestTemplate rt = new RestTemplate();
		// ResponseEntity<?> resultEntity =  new RestTemplate().postForEntity(
		//		"https://nid.naver.com/oauth2.0/token", requestBody, Map.class);
		
		ResponseEntity<String> tokenResponse = rt.exchange(
				"https://nid.naver.com/oauth2.0/token",
				HttpMethod.POST,
				naverTokenRequest,
				String.class
				);
				
		//Http 응답(JSON) -> 액세스 토큰 파싱
		String responseBody = tokenResponse.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		
		String access_token = jsonNode.get("access_token").asText();
		
		//결과적으로 받아온 토큰
		
		//어디서부터 오류가 났는지 삭제해보는것도 하나의 방법이다
		String header = "Bearer " + access_token;
		HttpHeaders profileRequestHeader = new HttpHeaders();
		profileRequestHeader.add("Authorization", header);
		
		HttpEntity<HttpHeaders> profileHttpEntity = new HttpEntity<>(profileRequestHeader);
		
		//profile api로 생성해둔 헤더를 담아서 요청을 보냅니다
		ResponseEntity<String> profileResponse = rt.exchange(
					"https://openapi.naver.com/v1/nid/me", 
							HttpMethod.POST,
							profileHttpEntity,
							String.class);
		
		System.out.println("profile response: "+profileResponse.getBody());
		
		String apiResult = profileResponse.getBody();//String형식의json데이터
		/* apiResult json구조 
		 * https://bumcrush.tistory.com/151
		 *  */
		//String형식인 apiResult를 json형태로 바꿈
		JSONObject rjson = new JSONObject(apiResult);
		JSONObject loginproducts = rjson.getJSONObject("response");
		
		System.out.println("api로 받아온 값은"+loginproducts);
		
		NaverInfResponse final_result = new NaverInfResponse(loginproducts);
		
		System.out.println("final_result는 "+final_result.toString());
		System.out.println("final_result에서 추출한 birthyear은 "+final_result.getBirthyear());
		
		String email = final_result.getEmail();
		String nickname = final_result.getNickname();
		String birthyear = final_result.getBirthyear();
		int get_birthyear = Integer.parseInt(birthyear);
		
		UserVO user = service.getUserByEmail(email);
		char adultYN;
		
		if(user==null) {
			UserVO newbie = new UserVO();//db <-> googleInfResponse: api를 통해가져온 값
			newbie.setUserEmail(email);
			newbie.setNickname(nickname);
			
			LocalDate now = LocalDate.now();
			int now_year = now.getYear();
			if(get_birthyear>(now_year-20)) {
				adultYN = 'N';
			}else {
				adultYN = 'Y';
				newbie.setAdultYN(adultYN);
			}
			
			service.insertUser(newbie);
			session.setAttribute("user", newbie);
			log.info("로그인이 되어있지 않아, 회원가입을 진행했고, 이메일은 다음과 같습니다" + email);
			return new RedirectView("comicnovel/home");
		}
		if(email!=null) {//로그인된 상태
			session.setAttribute("user", user);
			log.info("이미 로그인이 되어있습니다 : " + user.getUserEmail());
			return new RedirectView("comicnovel/home");
		}
		return new RedirectView("comicnovel/loginFail");//로그인되지 않은 상태
		
//
		
//		
//		String apiURL = "https://openapi.naver.com/v1/nid/me";
//        URL url = new URL(apiURL);
//        HttpURLConnection con = (HttpURLConnection)url.openConnection();
//        con.setRequestMethod("GET");
//        con.setRequestProperty("Authorization", header);
//        int responseCode = con.getResponseCode();
//        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//         
//        String inputLine = br.readLine();
//         
//        StringBuffer response = new StringBuffer();
//         
//        while(inputLine != null) {
//        	 response.append(inputLine);
//        }
//        br.close();
//        System.out.println(response.toString());

	}

}
