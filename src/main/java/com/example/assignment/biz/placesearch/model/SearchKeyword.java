package com.example.assignment.biz.placesearch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SearchKeyword {
	private String originKeyword; // 원래 키워드, 정렬 후 대체하여 출력값으로 나감
	private String sortKeyword;	// 정렬 시 사용할 키워드(originKeyword.replace(" ", ""))
	private int priorityOfCompany; // 기관 별 우선순위, 정렬 시 사용
	private int totalCount; // 전기관 통합 검색어 갯수, 기관 통합하여 정렬 시 활용
}
