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
public class Naver extends APIProvider{

	public Naver(RestTemplate restTemplate) {
		super(restTemplate);
	}
	
	@Value("${naver.id}")
    private String naverId;
	
	@Value("${naver.secret}")
    private String naverSecret;
	
	@Value("${naver.url}")
	private String url;
	
	/*
	 * 네이버의 경우 <b>와 같은 태그가 붙어오고, 장소명이 동일하게 오는 경우가 있으므로
	 * 태그 제거 및 중복 제거 진행 후 갯수를 세어준다.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int getSize(ResponseEntity<Map<String, Object>> response) {
		List<Map<String, Object>> items = (List<Map<String, Object>>)response.getBody().get("items");
		HashSet<String> set = new HashSet<String>();
		for(Map<String, Object> item : items) {
			String title = (String)item.get("title");
			title = title.replace("<b>", "");
			title = title.replace("</b>", "");
			set.add(title);
		}
		return set.size();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<String> makeResult(ResponseEntity<Map<String, Object>> response){
		List<String> result = new ArrayList<String>();
		List<Map<String, Object>> items = (List<Map<String, Object>>)response.getBody().get("items");
		for(Map<String, Object> item : items) {
			String title = (String)item.get("title");
			title = title.replace("<b>", "");
			title = title.replace("</b>", "");
			result.add(title);
		}
		return result;
	}


	@Override
	public ProviderInfo getProviderInfo() {
		ProviderInfo info = new ProviderInfo();
		
		HttpHeaders headers = new HttpHeaders(); 
		headers.set("X-Naver-Client-Id", naverId);
		headers.set("X-Naver-Client-Secret", naverSecret);
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		info.setUrl(url);
		info.setHeaders(headers);
		info.setSizeParam("display");
		
		return info;
	}

}
