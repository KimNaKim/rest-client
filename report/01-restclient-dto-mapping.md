# RestClient 학습 리포트 - 01. 응답 데이터 DTO 매핑

## 1. 학습 목표
- `RestClient`를 사용하여 HTTP GET 요청을 보내고, 수신된 JSON 데이터를 Java 객체(DTO)로 자동 매핑하는 문법을 익힌다.

## 2. 개발 환경 설정
### 의존성 (`build.gradle`)
- **Lombok**: `@Data`, `@Builder` 등을 사용하여 보일러플레이트 코드 제거
- **Spring Boot Starter Web**: `RestClient` 및 `Jackson` (JSON 매핑) 라이브러리 포함

### DTO 클래스 (`Post.java`)
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long userId;
    private Long id;
    private String title;
    private String body;
}
```

## 3. RestClient 설정 (`RestClientConfig.java`)
공통 설정(Base URL 등)을 포함한 `RestClient`를 빈으로 등록하여 재사용한다.
```java
@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }
}
```

## 4. 구현 코드 (`PostService.java`)
```java
public Post getPost() {
    return restClient.get()           // HTTP GET 메서드
            .uri("/posts/2")          // 호출 경로
            .retrieve()               // 실행 및 응답 수신
            .body(Post.class);        // JSON -> Post 객체 자동 변환
}
```

## 5. 주요 학습 포인트
- **자동 매핑**: `body(Post.class)`를 호출하면 Jackson 라이브러리가 JSON 필드명과 Java 필드명을 매칭하여 객체를 생성한다.
- **필수 조건**: DTO 클래스는 기본 생성자(`@NoArgsConstructor`)와 데이터를 채울 수 있는 접근자(`Getter/Setter`)가 필요하다.
- **가독성**: `RestTemplate`에 비해 체이닝(Chaining) 방식의 API를 제공하여 코드가 더 직관적이다.
