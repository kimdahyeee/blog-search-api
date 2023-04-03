# 🧐 블로그 검색 서비스

## 프로젝트 요구사항

### 블로그 검색 API

- [x] 키워드를 통해 블로그 검색 (카카오API, 네이버API 등 확장을 고려)
- [x] 검색 결과에서 Sorting (정확도 순, 최신 순) 기중 지원
- [x] 검색 결과는 pagination 형태로 제공

### 인기 검색어 제공 API

- [x] 사용자들이 많이 검색한 순서로, 최대 10개의 검색 키워드 제공
- [x] 검색어 별로 검색 횟수 제공

### 공통

- [x] 멀티 모듈 구성

## 🛠 프로젝트 환경 구성

> Java 11, spring boot, JPA, gradle, redis

- embeded h2 db 와 embeded redis 사용 (application 실행만으로 테스트하기 위함)
- 검색 순위를 빠르게 조회 하기 위해 `redis` 사용했습니다.

### 📂 모듈 구성

```
search-blog-api : 루트 프로젝트
└─ blog-api: 블로그 검색 관련 API 모듈
└─ core: 순수 자바로만 구성된 class 모듈
└─ api-core: api 에서 공통으로 사용하는 class 모듈
└─ domain-model: Entity, repository 관련한 데이터 관련 class 모듈
```

## API 명세

> 블로그 검색하기 API 를 명세합니다.

### 1. [공통] 요청 성공 시 응답

요청 성공 시 응답은 `data` 변수에 담겨 JSON 형태로 반환된다.

**example**

```
{
  "data" : {
  }
}
```

### 2. 블로그 검색하기 API

**기본 요청**

```
GET http://localhost:11002/api/v1/search/blog?keyword=댕댕이 HTTP/1.1
```

**Request**

| name | type | description | required |
| --- | --- | --- | --- |
| keyword | String | 검색 질의어 | O |
| sort | String | 결과 문서 정렬 방식, ACCURACY(정확도순) 또는 RECENCY(최신순), 기본 값 ACCURACY | O |
| page | Integer | 결과 페이지 번호, 1~50 사이의 값, 기본 값 1 | X |

**Response**

| name | type | description | required |
| --- | --- | --- | --- |
| blogResponses | Map | 검색 결과와 페이징 결과를 포함한다. | O |
| - pageNumber | Integer | 현재 페이지 | O |
| - totalCount | Integer | 전체 결과 건 수 | O |
| - datas | Array | 검색 결과 리스트 | O |
| -- title | String | 블로그 글 제목 | X |
| -- contents | String | 블로그 글 요약 | X |
| -- url | String | 블로그 글 URL | X |
| -- blogName | String | 블로그 이름 | X |
| -- thumbnail | String | 검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음 | X |
| -- dateTime | DateTime | 블로그 작성 시간 | X |

### 3. 블로그 검색하기 API

**기본 요청**

```
GET http://localhost:11002/api/v1/search/blog/keywords HTTP/1.1
```

**Response**

| name | type | description | required |
| --- | --- | --- | --- |
| topKeywords | Array | 인기 검색어 목록 | O |
| - keyword | String | 검색어 | X |
| - count | Integer | 검색 횟수 | X |

### 4. [공통] 요청 실패 시 응답

> 요청 실패 시 http 상태 코드 및 `json` 형식의 에러 응답 필드를 반환한다.

| name | type | description | required |
| --- | --- | --- | --- |
| status | Int | 에러코드 | O |
| message | String | 에러 메시지 | X |

**예 : 파라미터 오류 로 API 를 처리할 수 없는 경우**

```
HTTP/1.1 400 Bad Request  
{
  "status"  :400,
  "message" :"page is less than 1",
  "errors"  :[]
}
```

## 🤔 중점적으로 고민한 내용

- 인기 검색어 목록을 조회할 때, 트래픽이 높은 상황과 대용량 데이터 처리에 대해 고민했습니다.
    - 인기 검색어의 영구적인 보관을 위해 `rdbms` 와 빠른 조회를 위해 `redis` 를 사용
    - rdbms 에 저장할 시점에 데이터의 정확도를 보장하고 동시성 이슈를 방지하기 위해 pessimistic locking 락 모드를 적용
        - lock 을 획득하기 위해 대기하는 시간은 `3s` 로 설정
    - 인기 검색어 목록 (top 10) 에 대해서는 트래픽이 높을 것이라고 생각하여 검색 결과를 `30s` 간 캐싱했습니다.
- 외부 API (카카오 API) 장애 상황을 고려했습니다.
    - http code `4xx` 상황인 경우 `3회`, 딜레이 타임을 `2초`로 설정하여 재시도하도록 구현 (카카오 API 의 일시적인 오류 일 수도 있음을 고려)
        - `4xx` 외에도 외부 api 에서 내려주는 상세 코드를 사용해서 재시도 가능한 상태일 때에만 재시도하면 좋을 것 같음
        - 외부 api 의 장애 상황이 길어지는 경우를 고려하여, `3회` 이상 계속 재시도 실패하면 캐싱하여, 백업 API 를 호출하도록 개선 필요
    - 백업 API (네이버 API) 를 추가하기 위해 `BlogSearchClient` 인터페이스 및 request, response 객체 설계 (@todo 네이버 api 추가하면 내용 보완할 것)

## 실행 방법

```
java -jar blog-api-1.0.0-SNAPSHOT.jar
```

**샘플 API**

```
# 1. 블로그 검색
http://localhost:11002/api/v1/search/blog?keyword=댕댕이

# 2. 인기 검색어 목록
http://localhost:11002/api/v1/search/blog/keywords
```

## 빌드 결과물 링크
```
search-blog-api/build-result/blog-api-1.0.0-SNAPSHOT.jar
```
