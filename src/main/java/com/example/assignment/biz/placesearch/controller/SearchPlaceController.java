package com.example.assignment.biz.placesearch.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.assignment.biz.placesearch.service.SearchPlaceService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class SearchPlaceController {

	private final SearchPlaceService searchPlaceService; 
	
	@GetMapping("/place")
	public Map<String, Object> searchPlace(@RequestParam("keyword") String keyword) {
		return searchPlaceService.searchPlace(keyword);
	}
}
