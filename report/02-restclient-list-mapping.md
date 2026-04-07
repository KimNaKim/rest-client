# RestClient 학습 리포트 - 02. 리스트(List) 형태의 데이터 가져오기

## 1. 학습 목표
- JSON 배열 응답을 Java의 `List<T>` 형태의 제네릭 컬렉션으로 안전하게 매핑하는 방법을 익힌다.

## 2. 주요 개념: 타입 이레이저(Type Erasure)
- 자바 제네릭은 컴파일 타임에만 존재하고 런타임에는 정보가 사라진다.
- 따라서 `List<Post>.class`와 같은 문법은 사용할 수 없으며, 단순 `List.class`로 받으면 내부 요소가 무엇인지 알 수 없는 상태가 된다.

## 3. 해결책: ParameterizedTypeReference
- 스프링에서 제공하는 이 클래스를 사용하면 런타임에도 제네릭 타입 정보를 보존할 수 있다.
- 익명 내부 클래스 형식인 `new ParameterizedTypeReference<List<Post>>() {}`를 사용하여 타입을 지정한다.

## 4. 구현 코드 (`PostService.java`)
```java
/**
 * 게시물 목록 조회 (JSON Array -> List<Post>)
 */
public List<Post> getPosts() {
    return restClient.get()
            .uri("/posts")
            .retrieve()
            .body(new ParameterizedTypeReference<List<Post>>() {}); // 제네릭 타입 정보 유지
}
```

## 5. 주요 학습 포인트
- **제네릭 매핑**: 단순 클래스 타입이 아닌 복합 타입(List, Map 등)을 받을 때는 반드시 `ParameterizedTypeReference`를 사용해야 한다.
- **가독성**: 익명 클래스 문법이 생소할 수 있지만, 스프링 생태계에서 제네릭 타입을 다루는 가장 표준적이고 안전한 방식이다.
- **확장성**: `List<Post>` 뿐만 아니라 `Map<String, Object>` 등 모든 복합 제네릭 타입에 동일하게 적용 가능하다.
