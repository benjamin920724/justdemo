# 과제입니다


목차
----
- [구성 환경](#구성-환경)
- [빌드 및 실행](#빌드-및-실행)
- [테스트 방법](#테스트-방법)
- [기술 요구사항 구현 방안](#기술-요구사항-구현-방안)
- [참고사항](#참고사항)


구성 환경
----
- 기본 환경은 아래와 같습니다.
	- OS : Mac OS X
	- IDE : STS4
- 서버 환경은 아래와 같습니다.
	- Java11
	- Spring boot 2.6.3
	- gradle 7.4
	- 사용 라이브러리 :: 목적
		- H2 :: In-Memory DB로 사용
		- JPA :: JPA 인터페이스 사용 목적
		- Swagger :: 테스트 활용 목적
		- Lombok :: 생산성 향상 목적으로 사용
		- HttpClient :: RestAPI 호출 시 사용
		-  Junit5 :: 단위테스트
	
빌드 및 실행
----
### 터미널 환경
- Git, Java 설치 후 아래 명령어를 수행합니다.
```
$ git clone https://github.com/benjamin920724/justdemo.git
$ cd justdemo
$ ./gradlew clean build
$ java -jar build/libs/assignment-0.0.1-SNAPSHOT.jar
```

테스트 방법
----
- cURL을 활용합니다.
- Base URL : `http://localhost:8080`

1) 장소 검색하기

- Request 
<br/>`curl -G -X GET  "http://localhost:8080/api/place" --data-urlencode "keyword=[원하는키워드]"`
<br/><br/>동시성 테스트를 위해 동일한 단어를 여러 번 요청할 수 있습니다. 위 명령어를 `&`로 구분하여 요청 횟수만큼 반복해줍니다.
<br/>`curl & curl & curl & ...`

- Response (예시, keyword가 '강남'인 경우)
```
{
"places":
	[
	{"title":"압구정로데오거리"},
	{"title":"서울 선릉과 정릉"},
	{"title":"양재천"},
	{"title":"청담동명품거리"},
	{"title":"세븐럭카지노 강남코엑스점"},
	{"title":"코엑스"},
	{"title":"코엑스아쿠아리움"},
	{"title":"LG아트센터"},
	{"title":"가로수길"},
	{"title":"카페 노티드 청담"}
	]
}
```

2) 검색 키워드 목록

- Request
<br/>`curl -G -X GET "http://localhost:8080/api/hotkeyword"`

- Response
```
{
"keywords":[
	{
	"count":1,
	"keyword":"강남"
	}
	]
}
```

기술 요구사항 구현 방안
----
- 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 설계 및 구현 (예시.키워드 별로 검색된 횟수)
	- 장소 검색 시, 해당 키워드 검색 횟수를 Update 하는 과정에서 동시성 이슈가 발생할 수 있기 때문에 비관적 락을 사용하여 동시성 이슈를 고려하였습니다.
	- 만일 한 번도 검색된 적 없는 키워드가 동시에 요청으로 들어오면, Exception 처리하여 예외 메시지를 제공해줍니다.
	
- 카카오, 네이버 등 검색 API 제공자의 다양한 장애 발생 상황에 대한 고려
	- 제공자마다 Timeout 또는 예외적인 Server Error가 발생할 수 있고, 요건 상, 특정 기관이 장애가 발생해도 검색 결과에 미포함일 뿐, 검색 서비스는 정상적으로 동작해야 한다고 판단하여 Exception을 Throw하지 않고 다음 프로세스를 진행할 수 있도록 하였습니다.

- 구글 장소 검색 등 새로운 검색 API 제공자의 추가 시 변경 영역 최소화에 대한 고려
	- 제공자 수에 영향을 받지 않는 정렬 알고리즘을 구현하였습니다.
	- 새로운 검색 API 제공자 추가 시 구현 편의성을 위해 APIProvider 라는 추상클래스를 구현하였습니다.

- 서비스 오류 및 장애 처리 방법에 대한 고려
	- 논리적으로 장애를 무시해야 하는 부분은 무시하고, 그 외 처리가 필요한 부분에 대해서는 예외 처리하였습니다.
	- CustomException 생성 및 ExceptionController 사용을 통해 필요 시 간단하게 오류를 핸들링할 수 있습니다.


참고사항
----
- 특정 서비스 검색 결과가 5개 미만일 시, 가장 마지막 음절을 제외하고 다시 한 번 검색하도록 하였습니다. (1회만)
- 검색어 길이는 양 끝 공백을 제외한 1~50 자로 제한하였습니다.




