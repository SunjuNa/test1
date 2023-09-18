package com.megait.comicnovel.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.XML;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.megait.comicnovel.bean.vo.AladinNewBookPrdDTO;
import com.megait.comicnovel.bean.vo.AladinNewBookReqDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewBookService {
	public List<AladinNewBookPrdDTO> aladinNewBookAPI(AladinNewBookReqDTO newbookVar) throws JSONException{
		//aladin api url
		String url = "http://www.aladin.co.kr/ttb/api/ItemList.aspx";
		URI uri = UriComponentsBuilder.fromHttpUrl(url)
							.path("")
							.queryParam("ttbkey", newbookVar.getTtbkey())
							.queryParam("QueryType", newbookVar.getQueryType())
							.queryParam("MaxResults", newbookVar.getMaxResults())
							.queryParam("start", newbookVar.getStart())
							.queryParam("SearchTarget", newbookVar.getSearchTarget())
							.queryParam("output", newbookVar.getOutput())
							.queryParam("Version", newbookVar.getVersion())
							.queryParam("CategoryId", newbookVar.getCategoryId())
							.queryParam("Cover", newbookVar.getCover())
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
		 List<AladinNewBookPrdDTO> newBookPrdDTOs = fromXMLtoAladinNewBookPrd(result.getBody());
		 
		 log.info("result={}", newBookPrdDTOs);
		 return newBookPrdDTOs;
	}
	private List<AladinNewBookPrdDTO> fromXMLtoAladinNewBookPrd(String result) throws JSONException{
		//여기서 result는 xml코드이다 
		//XML을 JSON으로 변환하기
		org.json.JSONObject jobj = XML.toJSONObject(result);
		
		//변환된 데이터 확인
		//object<<item><item>...>
		log.info("-----------------jobj.toString()-------------");
		log.info(jobj.toString());
		
		org.json.JSONArray newBookProducts = jobj.getJSONObject("object").getJSONArray("item");
		
		log.info("----------item----------");
		log.info(newBookProducts.toString());
		
		List<AladinNewBookPrdDTO> aladinNewBookPrdDTOList = new ArrayList<>();
		
		for(int i=0; i<newBookProducts.length(); i++) {
			//{"author":~~},{"author":~~}
			org.json.JSONObject aladinnewBookJson = (org.json.JSONObject)newBookProducts.get(i);
			AladinNewBookPrdDTO itemDTO = new AladinNewBookPrdDTO(aladinnewBookJson);
			aladinNewBookPrdDTOList.add(itemDTO);
		}
		return aladinNewBookPrdDTOList;
	}
}
