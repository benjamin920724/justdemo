package com.example.assignment.biz.placesearch.common.restapi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.assignment.biz.placesearch.model.ProviderInfo;

@Component
public class Kakao extends APIProvider{	
	
	public Kakao(RestTemplate restTemplate) {
		super(restTemplate);
	}

	@Value("${kakao.id}")
    private String kakaoId;
	
	@Value("${kakao.url}")
	private String url;
	
	@SuppressWarnings("unchecked")
	@Override
	public int getSize(ResponseEntity<Map<String, Object>> response) {
		List<Map<String, Object>> items = ((List<Map<String, Object>>)response.getBody().get("documents"));
		HashSet<String> set = new HashSet<String>();
		for(Map<String, Object> item : items) {
			String title = (String)item.get("place_name");
			set.add(title);
		}
		return set.size();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> makeResult(ResponseEntity<Map<String, Object>> response){
		List<String> result = new ArrayList<String>();
		List<Map<String, Object>> items = (List<Map<String, Object>>)response.getBody().get("documents");
		for(Map<String, Object> item : items) {
			result.add((String)item.get("place_name"));
		}
		return result;
	}

	@Override
	public ProviderInfo getProviderInfo() {
		ProviderInfo info = new ProviderInfo();
		
		HttpHeaders headers = new HttpHeaders(); 
		headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoId);
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		info.setUrl(url);
		info.setHeaders(headers);
		info.setSizeParam("size");
		
		return info;
	}

}
