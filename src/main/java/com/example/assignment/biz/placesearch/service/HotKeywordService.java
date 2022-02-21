package com.example.assignment.biz.placesearch.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.assignment.biz.placesearch.common.enums.ErrorMessage;
import com.example.assignment.biz.placesearch.common.exception.CustomException;
import com.example.assignment.biz.placesearch.common.utils.Constants;
import com.example.assignment.biz.placesearch.dao.HotKeyword;
import com.example.assignment.biz.placesearch.dao.HotKeywordRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HotKeywordService {

	@Autowired
	HotKeywordRepository hotKeywordRepository;
	
	@Transactional
	public Map<String, Object> hotKeywordService(){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<HotKeyword> list = hotKeywordRepository.findAll();
			sort(list);
			List<Map<String, Object>> keywords = makeKeywords(list);
			result.put("keywords", keywords);
		} catch(RuntimeException e) {
			log.error("exception in hotKeywordService => {}", e);
			throw new CustomException(ErrorMessage.SERVER_ERROR.getMsg());
		}
		return result;
	}
	private void sort(List<HotKeyword> list) {
		Collections.sort(list, new Comparator<HotKeyword>() {
			public int compare(HotKeyword o1, HotKeyword o2) {
				if(o1.getCount() > o2.getCount()) {
					return -1;
				} else {
					
					return 0;
				}
			}
		});
	}

	private List<Map<String, Object>> makeKeywords(List<HotKeyword> list){
		List<Map<String, Object>> keywords = new ArrayList<Map<String, Object>>();
		int size = list.size() > Constants.hotkeywordsLimitSize ? Constants.hotkeywordsLimitSize : list.size();
		for(int i=0; i<size; i++) {
			HotKeyword keyword = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("keyword", keyword.getKeyword());
			map.put("count", keyword.getCount());
			keywords.add(map);
		}
		return keywords;
	}
}
