package com.example.assignment;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.assignment.biz.placesearch.common.utils.DeduplicationUtils;
import com.example.assignment.biz.placesearch.model.SearchKeyword;

import ch.qos.logback.classic.Logger;

@SpringBootTest
public class SortingTest {

	Logger log = (Logger) LoggerFactory.getLogger(SortingTest.class);
	
	private Map<String, Integer> totalWordCount = new HashMap<String, Integer>();
	private List<String> fakeKakao = new ArrayList<String>();
	private List<String> fakeNaver = new ArrayList<String>();
	private List<String> fakeGoogle = new ArrayList<String>();
	
	@BeforeEach
	public void setup() {
		
		fakeKakao.add("A곱");
		fakeKakao.add("B곱");
		fakeKakao.add("C곱");
		fakeKakao.add("D곱");
		
		fakeNaver.add("A곱");
		fakeNaver.add("E곱");
		fakeNaver.add("D곱");
		fakeNaver.add("C곱");
		
		fakeGoogle.add("A곱");
		fakeGoogle.add("C곱");
		fakeGoogle.add("F곱");
		fakeGoogle.add("G곱");
	}
	
	@Test
	public void makeTotalWordCountMapOf2() {
		
		Map<String, Integer> compare = new HashMap<String, Integer>();
		compare.put("A곱", 2);
		compare.put("C곱", 2);
		compare.put("D곱", 2);
		compare.put("B곱", 1);
		compare.put("E곱", 1);
		
		for(String keyword : fakeKakao){
			int currentCount = totalWordCount.get(keyword) == null ? 0 : totalWordCount.get(keyword);
			totalWordCount.put(keyword, currentCount+1);
		}
		
		for(String keyword : fakeNaver){
			int currentCount = totalWordCount.get(keyword) == null ? 0 : totalWordCount.get(keyword);
			totalWordCount.put(keyword, currentCount+1);
		}
		
		assertTrue(totalWordCount.equals(compare));
	}
	
	@Test
	public void sort() {

		List<SearchKeyword> keywordList = new ArrayList<SearchKeyword>();
		
		for(String keyword : fakeKakao){
			int currentCount = totalWordCount.get(keyword) == null ? 0 : totalWordCount.get(keyword);
			totalWordCount.put(keyword, currentCount+1);
		}
		
		for(String keyword : fakeNaver){
			int currentCount = totalWordCount.get(keyword) == null ? 0 : totalWordCount.get(keyword);
			totalWordCount.put(keyword, currentCount+1);
		}
		
		for(String keyword : fakeGoogle){
			int currentCount = totalWordCount.get(keyword) == null ? 0 : totalWordCount.get(keyword);
			totalWordCount.put(keyword, currentCount+1);
		}
		
		
		
		for(String keyword : fakeKakao){
			SearchKeyword kakao = new SearchKeyword(keyword, keyword, 1, totalWordCount.get(keyword));
			keywordList.add(kakao);
		}
		
		for(String keyword : fakeNaver){
			SearchKeyword naver = new SearchKeyword(keyword, keyword, 2, totalWordCount.get(keyword));
			keywordList.add(naver);
		}
		
		for(String keyword : fakeGoogle){
			SearchKeyword google = new SearchKeyword(keyword, keyword, 3, totalWordCount.get(keyword));
			keywordList.add(google);
		}
		
		log.info("totalWordCount => {}", totalWordCount);
		log.info("keywordList => {}", keywordList);
		
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
		
		log.info("keywordList => {}", keywordList);
		List<SearchKeyword> distinct = DeduplicationUtils.deduplication(keywordList, SearchKeyword::getSortKeyword);
		log.info("distinct => {}", distinct);
	}
	
	@Test
	public void compareBank() {
		
		// given
		List<String> compare = new ArrayList<String>();
		compare.add("카카오뱅크");
		compare.add("국민은행");
		compare.add("부산은행");
		compare.add("우리은행");
		compare.add("새마을금고");
		compare.add("하나은행");
		compare.add("기업은행");
		
		
		List<String> kakaoResult = new ArrayList<String>();
		kakaoResult.add("카카오뱅크");
		kakaoResult.add("우리은행");
		kakaoResult.add("국민은행");
		kakaoResult.add("부산은행");
		kakaoResult.add("새마을금고");
		
		List<String> naverResult = new ArrayList<String>();
		naverResult.add("카카오뱅크");
		naverResult.add("부산은행");
		naverResult.add("하나은행");
		naverResult.add("국민은행");
		naverResult.add("기업은행");
		
		
		// when
		Map<String, Integer> totalWordCount = new HashMap<String, Integer>();
		addTotalWordCountMap(totalWordCount, kakaoResult);
		addTotalWordCountMap(totalWordCount, naverResult);
		
		List<SearchKeyword> keywordList = new ArrayList<SearchKeyword>();
		for(String kakaoword : kakaoResult){
			SearchKeyword kakao = new SearchKeyword(kakaoword, kakaoword.replace(" ", ""), 1, totalWordCount.get(kakaoword));
			keywordList.add(kakao);
		}
		
		for(String naverword : naverResult){
			SearchKeyword naver = new SearchKeyword(naverword, naverword.replace(" ", ""), 2, totalWordCount.get(naverword));
			keywordList.add(naver);
		}
		
		sort(keywordList);
		List<SearchKeyword> distinct = DeduplicationUtils.deduplication(keywordList, SearchKeyword::getSortKeyword);
		List<String> target = new ArrayList<String>();
		for(int i=0; i< distinct.size(); i++) {
			SearchKeyword searchKeyword = distinct.get(i);
			target.add(searchKeyword.getOriginKeyword());
		}
		
		// then
 		assertArrayEquals(compare.toArray(), target.toArray());
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
	
}
