package com.megait.comicnovel.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.json.XML;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.megait.comicnovel.bean.vo.AladinProductDTO;
import com.megait.comicnovel.bean.vo.AladinReqVarDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookSearchService {
	public List<AladinProductDTO> aladinShopSearchAPI(AladinReqVarDTO aladinVar) throws JSONException {
		//참고
		//https://wonin.tistory.com/499
		//aladin api url
		 String url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
		 URI uri = UriComponentsBuilder.fromHttpUrl(url)
		 							.path("")
		 							.queryParam("Query", aladinVar.getQuery())
		 							.queryParam("ttbkey", aladinVar.getTtbkey())
		 							.queryParam("QueryType", aladinVar.getQueryType())
		 							.queryParam("MaxResults", aladinVar.getMaxResults())
		 							.queryParam("start", aladinVar.getStart())
		 							.queryParam("SearchTarget", aladinVar.getSearchTarget())
		 							.queryParam("output", aladinVar.getOutput())
		 							.queryParam("Version", aladinVar.getVersion())
		 							.encode()
		 							.build()
		 							.toUri();
		 log.info("uri: {}", uri);
		 
		 //Spring boot에서 제공하는 RestTemplate
		 RestTemplate restTemplate = new RestTemplate();
		 
		 RequestEntity<Void> req = RequestEntity
				 	.get(uri)
				 	.build();
		 
		 //1.api를 호출하여 결과 가져오기
		 //RestTemplate.getForObject(URI uri, Class<T> responseType) => (호출하는 url, 반환타입)
		 ResponseEntity<String> result = restTemplate.exchange(req, String.class);
		 List<AladinProductDTO> aladinProductDTOs = fromXMLtoAladinProduct(result.getBody());
		 
		 log.info("result={}", aladinProductDTOs);
		 return aladinProductDTOs;
	}
	private List<AladinProductDTO> fromXMLtoAladinProduct(String result) throws JSONException{
		 //여기서 result는 xml코드이다 
		 //XML을 JSON으로 변환하기
		 org.json.JSONObject jobj = XML.toJSONObject(result);
		 
		 //변환된 데이터 확인
		 //object<<item><item>...>
		 log.info("-----------------jobj.toString()-------------");
		 log.info(jobj.toString());
		 
		 org.json.JSONArray aladinProducts = jobj.getJSONObject("object").getJSONArray("item");
		 
		 log.info("----------item----------");
		 log.info(aladinProducts.toString());
		 log.info("aladinproducts개수: "+aladinProducts.length());
		 
		 List<AladinProductDTO> aladinProductDTOList = new ArrayList<>();
		 
		 for(int i=0; i<aladinProducts.length(); i++) {
			 //{"author":~~},{"author":~~}
			 org.json.JSONObject aladinProductsJson = (org.json.JSONObject) aladinProducts.get(i);
			 AladinProductDTO itemDTO = new AladinProductDTO(aladinProductsJson);
			 aladinProductDTOList.add(itemDTO);
		 }
		 
	 return aladinProductDTOList;
	}
		
		
		
}

