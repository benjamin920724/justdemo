package com.example.assignment.common.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(){
		
		//Apache HttpComponents HttpClient 
		HttpClient httpClient = HttpClientBuilder.create() 
									.setMaxConnTotal(20) // max 커넥션 수 
									.setMaxConnPerRoute(10) // 호스트당 커넥션 풀에 생성가능한 커넥션 수
									.build(); 
				
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(); 
		factory.setReadTimeout(5000); // read timeout 
		factory.setConnectTimeout(3000); // connection timeout 
		factory.setHttpClient(httpClient); 
		
		RestTemplate restTemplate = new RestTemplate(factory);
		
		return restTemplate;


	}
}

