package com.example.assignment.biz.placesearch.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.assignment.biz.placesearch.common.enums.ErrorMessage;
import com.example.assignment.biz.placesearch.common.enums.ProviderPriority;
import com.example.assignment.biz.placesearch.common.exception.CustomException;
import com.example.assignment.biz.placesearch.common.restapi.Kakao;
import com.example.assignment.biz.placesearch.common.restapi.Naver;
import com.example.assignment.biz.placesearch.common.utils.DeduplicationUtils;
import com.example.assignment.biz.placesearch.dao.HotKeyword;
import com.example.assignment.biz.placesearch.dao.HotKeywordRepository;
import com.example.assignment.biz.placesearch.model.SearchKeyword;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class SearchPlaceService {
	
	private final Kakao kakao;
	private final Naver naver;
	private final HotKeywordRepository hotKeywordRepository;
	
	public Map<String, Object> searchPlace(String keyword) {
		log.info("start searchPlace Service");
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			keyword = keyword.trim();
			
			// 입력값 검증
			inputValidation(keyword);
			
			// 사용자 검색어를 저장
			saveKeyword(keyword);
			
			// 검색API제공자 별 검색, 기관 별로 타임아웃 등 응답이 없어도 서비스 동작 가능해야 함
			Map<String, Integer> totalWordCount = new HashMap<String, Integer>();
			List<SearchKeyword> keywordList = new ArrayList<SearchKeyword>();
			try {
				List<String> kakaoResult = kakao.process(keyword);
				addTotalWordCountMap(totalWordCount, kakaoResult);
				for(String kakaoword : kakaoResult){
					SearchKeyword kakao = new SearchKeyword(kakaoword, kakaoword.replace(" ", ""), ProviderPriority.Kakao.priority(), totalWordCount.get(kakaoword));
					keywordList.add(kakao);
				}
			} catch(RuntimeException e) {
				log.error("Kakao Exception => {}", e);
			}
			
			try {
				List<String> naverResult = naver.process(keyword);
				addTotalWordCountMap(totalWordCount, naverResult);
				for(String naverword : naverResult){
					SearchKeyword naver = new SearchKeyword(naverword, naverword.replace(" ", ""), ProviderPriority.Naver.priority(), totalWordCount.get(naverword));
					keywordList.add(naver);
				}
			} catch(RuntimeException e) {
				log.error("Naver Exception => {}", e);
			}
			
			List<Map<String, Object>> places = new ArrayList<Map<String, Object>>();
			if(keywordList.size() > 0) {
				// 정렬
				sort(keywordList); 
				// 중복 element 제거
				List<SearchKeyword> distinctList = DeduplicationUtils.deduplication(keywordList, SearchKeyword::getSortKeyword); 
				// 출력데이터 생성
				places = makePlaceList(distinctList); 
			}
			response = makeResponse(places);
		} catch(RuntimeException e) {
			log.error("exception in searchPlace => {}", e);
			throw e;
		}
		return response;
	}
	
	private void inputValidation(String keyword) {
		if(keyword.length() < 1 || keyword.length() > 50) {  // 50자 제한은 임의 지정.
			throw new CustomException(ErrorMessage.INVALID_INPUT.getMsg());
		}
	}
	
	@Transactional
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	private void saveKeyword(String keyword) {
		try {
			HotKeyword entity = hotKeywordRepository.findByIdForUpdate(keyword);
			if(entity == null) {
				initSave(keyword);
			} else {
				int count = entity.getCount();
				entity.setCount(count+1);
				hotKeywordRepository.save(entity);
			}
			
		} catch(Exception e) {
			log.error("error in saveKeyword => {}", e);
		}
	}
	
	@Transactional
	private void initSave(String keyword) {
		try {
			HotKeyword entity = new HotKeyword();
			entity.setKeyword(keyword);
			entity.setCount(1);
			hotKeywordRepository.save(entity);
		} catch(Exception e) {
			log.error("error in initSave => {}", e);
		}
	}
	
	private void addTotalWordCountMap(Map<String, Integer> totalWordCount, List<String> list) {
		for(String keyword : list){
			int currentCount = totalWordCount.get(keyword) == null ? 0 : totalWordCount.get(keyword);
			totalWordCount.put(keyword, currentCount+1);
		}
	}
	
	private void sort(List<SearchKeyword> keywordList) {
		Collections.sort(keywordList, new Comparator<SearchKeyword>() {
			public int compare(SearchKeyword w1, SearchKeyword w2) {
				if(w1.getTotalCount() > w2.getTotalCount()) {
					return -1;
				} else {
					if(w1.getPriorityOfCompany() < w2.getPriorityOfCompany()) {
						return -1;
					}
					return 0;
				}
			}
		});
	}
	
	private List<Map<String, Object>> makePlaceList(List<SearchKeyword> list){
		List<Map<String, Object>> places = new ArrayList<Map<String, Object>>();
		for(int i=0; i<list.size(); i++) {
			SearchKeyword searchKeyword = list.get(i);
			Map<String,Object> place = new HashMap<String, Object>();
			place.put("title", searchKeyword.getOriginKeyword());
			places.add(place);
		}
		return places;
	}
	
	private Map<String, Object> makeResponse(List<Map<String, Object>> places){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("places", places);
		
		return result;
	}

}
