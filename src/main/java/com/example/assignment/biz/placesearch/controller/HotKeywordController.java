package com.example.assignment.biz.placesearch.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.assignment.biz.placesearch.service.HotKeywordService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class HotKeywordController {

	private final HotKeywordService hotKeywordService;
	
	@GetMapping("/hotkeyword")
	public Map<String, Object> hotKeyword(){
		return hotKeywordService.hotKeywordService();
	}
}
