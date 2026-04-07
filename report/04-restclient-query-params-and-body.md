# RestClient 학습 리포트 - 04. 쿼리 파라미터와 요청 바디(Body) 전송

## 1. 학습 목표
- URL 뒤에 동적인 파라미터를 추가하는 방법(Query Parameter)과 서버로 데이터를 전송하는 방법(Request Body)을 익힌다.

## 2. 주요 개념

### (1) 쿼리 파라미터 (Query Parameter)
- `uri(uriBuilder -> ...)` 형식을 사용하여 URL에 `?key=value` 형태의 파라미터를 안전하게 추가할 수 있다.
- 문자열 연산보다 가독성이 좋고, 특수 문자 인코딩 등을 스프링이 자동으로 처리해 준다.

### (2) 요청 바디 (Request Body)
- HTTP POST, PUT 요청 시 데이터를 전송하기 위해 사용한다.
- `.body(Object)` 메서드에 Java 객체를 전달하면, Jackson 라이브러리가 이를 JSON 문자열로 자동 변환(직렬화)하여 전송한다.

## 3. 구현 코드 (`PostService.java`)

### 쿼리 파라미터 조회
```java
public List<Post> getPostsByUserId(Long userId) {
    return restClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/posts")
                    .queryParam("userId", userId) // ?userId=1
                    .build())
            .retrieve()
            .body(new ParameterizedTypeReference<List<Post>>() {});
}
```

### 데이터 생성 (POST)
```java
public Post createPost(Post newPost) {
    return restClient.post()
            .uri("/posts")
            .body(newPost) // Java 객체를 JSON으로 자동 변환하여 전송
            .retrieve()
            .body(Post.class);
}
```

## 4. 주요 학습 포인트
- **안전한 URI 구성**: `uriBuilder`를 통해 복잡한 쿼리 파라미터 조합을 실수 없이 작성할 수 있다.
- **자동 직렬화**: 개발자가 직접 JSON 문자열을 만들 필요 없이, DTO 객체만 넘기면 HTTP Body에 데이터가 담긴다.
- **RESTful 설계**: HTTP 메서드(`get()`, `post()`)와 URI 구조를 통해 API의 의도를 명확히 표현할 수 있다.
