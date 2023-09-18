package com.megait.comicnovel.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.megait.comicnovel.bean.vo.NaverSearchDTO;
import com.megait.comicnovel.bean.vo.NaverSearchPrdDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NaverSearchService {
	public List<NaverSearchPrdDTO> naverSearchAPI(NaverSearchDTO naverReqDTO){
		String url = "https://openapi.naver.com/v1/search/blog";
		URI uri = UriComponentsBuilder.fromHttpUrl(url)
				.path("")
				.queryParam("query", naverReqDTO.getQuery())
				.queryParam("display", naverReqDTO.getDisplay())
				.queryParam("start", naverReqDTO.getStart())
				.encode()
				.build()
				.toUri();
		log.info("uri:{}", uri);
		
		RestTemplate restTemplate = new RestTemplate();
		
		RequestEntity<Void> req = RequestEntity
	                .get(uri)
	                .header("X-Naver-Client-Id", "")
	                .header("X-Naver-Client-Secret", "")
	                .build();
		ResponseEntity<String> result = restTemplate.exchange(req, String.class);
        List<NaverSearchPrdDTO> naverProductDTOs = fromJSONtoNaverProduct(result.getBody());
		
		log.info("result={}" + naverProductDTOs);
		
		return naverProductDTOs;
	}

	private List<NaverSearchPrdDTO> fromJSONtoNaverProduct(String result) {
		// TODO Auto-generated method stub
		// 문자열 정보를 JSONObject로 바꾸기
        JSONObject rjson = new JSONObject(result);
        // JSONObject에서 items 배열 꺼내기
        // JSON 배열이기 때문에 보통 배열이랑 다르게 활용해야한다.
        org.json.JSONArray naverProducts = rjson.getJSONArray("items");
        
        List<NaverSearchPrdDTO> naverProductDtoList = new ArrayList<>();
        for (int i = 0; i < naverProducts.length(); i++) {
            JSONObject naverProductsJson = (JSONObject) naverProducts.get(i);
            NaverSearchPrdDTO itemDto = new NaverSearchPrdDTO(naverProductsJson);
            naverProductDtoList.add(itemDto);
        }
        
        return naverProductDtoList;
		
	}
}
