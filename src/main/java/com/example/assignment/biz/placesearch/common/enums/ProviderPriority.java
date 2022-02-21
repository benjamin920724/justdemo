package com.example.assignment.biz.placesearch.common.enums;

public enum ProviderPriority {
	
	Kakao(1),
	Naver(2);
	
	ProviderPriority(int priority) {
		this.priority = priority;
	}
	
	public final int priority;
	public int priority() {return priority;}
	
	
}
