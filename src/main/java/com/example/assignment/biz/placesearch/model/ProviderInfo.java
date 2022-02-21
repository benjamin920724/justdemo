package com.example.assignment.biz.placesearch.model;

import org.springframework.http.HttpHeaders;

import lombok.Data;

@Data
public class ProviderInfo {

	private HttpHeaders headers;
	private String url;
	private String sizeParam;
}
