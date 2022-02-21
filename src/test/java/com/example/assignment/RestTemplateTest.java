package com.example.assignment;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ch.qos.logback.classic.Logger;



@SpringBootTest
public class RestTemplateTest {
	Logger log = (Logger) LoggerFactory.getLogger(RestTemplateTest.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@BeforeEach
	public void setup() {
		restTemplate = new RestTemplate();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void exchangeGet_kakaoPlace() throws Exception {
		
		String url = "https://dapi.kakao.com/v2/local/search/keyword.json";
		HttpHeaders headers = new HttpHeaders(); 
		headers.set("Authorization", "KakaoAK 39657f32752e187ca3c9b28c5e6a507b");
		
		Charset utf8 = Charset.forName("UTF-8"); 
		MediaType mediaType = new MediaType("application", "json", utf8); 
		headers.setContentType(mediaType);
			
		HttpEntity<String> request = new HttpEntity<String>(headers); //adding the query params to the URL 
		
		URI uri = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("query", "신환아파트")
				.queryParam("size", 5)
				.build()
				.encode()
				.toUri();
		
		ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, request, Map.class);
		log.info("kakao response => {}", response);
		log.info("list => {}", response.getBody().get("documents"));
		List<Map> items = (List)response.getBody().get("documents");
		for(Map item : items) {
			log.info("kakao item => {}", item);
			log.info("place_name => {}", item.get("place_name"));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void exchangeGet_naverPlace() throws Exception {
		
		try {
			String url = "https://openapi.naver.com/v1/search/local.json";
			HttpHeaders headers = new HttpHeaders(); 
			headers.set("X-Naver-Client-Id", "HZUyIjQHWkfOKMy03BBo");
			headers.set("X-Naver-Client-Secret", "XKmri36pWC");
			
			Charset utf8 = Charset.forName("UTF-8"); 
			MediaType mediaType = new MediaType("application", "json", utf8); 
			headers.setContentType(mediaType);
				
			HttpEntity<String> request = new HttpEntity<String>(headers); //adding the query params to the URL 
			
			URI uri = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("query", "곱")
					.queryParam("display", 5)
					.build()
					.encode()
					.toUri();
			
			ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, request, Map.class);
			log.info("naver response => {}", response);
			log.info("naver items => {}", response.getBody().get("items"));
			log.info("naver total => {}", response.getBody().get("total"));
			List<Map> items = (List)response.getBody().get("items");
			for(Map item : items) {
				log.info("naver item => {}", item);
				log.info("title => {}", item.get("title"));
				
				String title = (String)item.get("title");
				title = title.replace("<b>", "");
				title = title.replace("</b>", "");
				log.info("title parsed => {}", title);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
