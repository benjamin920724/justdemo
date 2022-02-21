package com.example.assignment.biz.placesearch.common.restapi;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.assignment.biz.placesearch.common.utils.Constants;
import com.example.assignment.biz.placesearch.model.ProviderInfo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class APIProvider {
	
	private final RestTemplate restTemplate;
	
	public List<String> process(String keyword) {
		try {
			// 검색
			ProviderInfo providerInfo = getProviderInfo();
			ResponseEntity<Map<String, Object>> response = search(providerInfo, keyword);
			
			// 검색결과가 5개 이하면 검색어 마지막 글자를 제외하고 재검색 1회 실행
			int resultSize = getSize(response);
			if (keyword.length() > 1 && isLessThanTargetSize(resultSize)) {
				keyword = makeKeywordShorter(keyword);
				response = search(providerInfo, keyword);
			}
			// 검색결과 Parsing 후 List로 만들어서 반환
			List<String> result = makeResult(response);
			
			return result.stream().distinct().collect(Collectors.toList());
			
		} catch(RuntimeException e) {
			throw e;
		}
	}
	
	private ResponseEntity<Map<String, Object>> search(ProviderInfo info, String keyword){
		
		HttpEntity<String> request = new HttpEntity<String>(info.getHeaders());
		URI uri = uriBuilder(info, keyword);
		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<Map<String, Object>>(){
		});
		
		return response;
	}
	
	private URI uriBuilder(ProviderInfo info, String keyword) {
		URI uri = UriComponentsBuilder.fromHttpUrl(info.getUrl())
						.queryParam("query", keyword)
						.queryParam(info.getSizeParam(), Constants.tatgetSize)
						.build()
						.encode()
						.toUri();
		return uri;
	}
	
	private boolean isLessThanTargetSize(int searchResultSize) {
		return Constants.tatgetSize > searchResultSize;
	}
	
	private String makeKeywordShorter(String keyword) {
		return keyword.substring(0, keyword.length() -1);	
	}
	
	protected abstract ProviderInfo getProviderInfo();
	protected abstract List<String> makeResult(ResponseEntity<Map<String, Object>> response);
	protected abstract int getSize(ResponseEntity<Map<String, Object>> response);
}
