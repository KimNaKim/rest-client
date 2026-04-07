# RestClient 학습 리포트 - 03. 에러 핸들링 (onStatus)

## 1. 학습 목표
- API 호출 시 발생할 수 있는 오류 상태 코드(4xx, 5xx)를 감지하고, 이에 대한 사용자 정의 예외 처리 방법을 익힌다.

## 2. 주요 개념: onStatus
- `RestClient`는 기본적으로 오류 응답 시 `RestClientResponseException`을 던지지만, `.onStatus()`를 사용하면 이를 가로채서 원하는 로직을 수행할 수 있다.
- **첫 번째 인자**: 상태 코드를 판별할 조건 (Predicate). 예: `HttpStatusCode::is4xxClientError`
- **두 번째 인자**: 에러 발생 시 실행할 핸들러 (BiConsumer). `(request, response)` 객체에 접근 가능하다.

## 3. 구현 코드 (`PostService.java`)
```java
/**
 * 에러 핸들링이 포함된 게시물 조회
 */
public Post getPostWithCheck(Long id) {
    return restClient.get()
            .uri("/posts/{id}", id)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                // 클라이언트 오류 (400, 404 등) 처리
                throw new RuntimeException("게시글을 찾을 수 없습니다. (ID: " + id + ")");
            })
            .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                // 서버 오류 (500 등) 처리
                throw new RuntimeException("서버 오류가 발생했습니다.");
            })
            .body(Post.class);
}
```

## 4. 주요 학습 포인트
- **세밀한 제어**: 여러 개의 `.onStatus()`를 체이닝하여 상태 코드별로 다른 예외나 로그 처리가 가능하다.
- **응답 정보 활용**: 핸들러 내부에서 `response.getStatusCode()`, `response.getBody()` 등을 사용하여 상세한 에러 원인을 파악할 수 있다.
- **가독성**: 예외 처리 로직이 비즈니스 로직(조회) 흐름 안에 선형적으로 작성되어 코드 파악이 쉽다.
