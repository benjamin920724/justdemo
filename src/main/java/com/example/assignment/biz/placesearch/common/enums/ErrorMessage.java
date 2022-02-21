package com.example.assignment.biz.placesearch.common.enums;

public enum ErrorMessage {

	INVALID_INPUT("1~50자 사이의 검색어를 입력해주세요"),
	SERVER_ERROR("서버가 원활하지 않습니다. 잠시 후 다시 시도해주세요.");
	
	private final String msg;
	
	ErrorMessage(String msg){
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
	
	
}
