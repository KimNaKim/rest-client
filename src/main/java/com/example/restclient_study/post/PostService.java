package com.example.restclient_study.post;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final RestClient restClient;

    /**
     * 1. 단일 게시물 조회 (JSON -> Java Object)
     */
    public Post getPost() {
        return restClient.get()
                .uri("/posts/2")
                .retrieve()
                .body(Post.class);
    }

    /**
     * 2. 게시물 목록 조회 (JSON Array -> List<Post>)
     * ParameterizedTypeReference를 사용하여 제네릭 타입 정보를 유지합니다.
     */
    public List<Post> getPosts() {
        return restClient.get()
                .uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Post>>() {});
    }

    /**
     * 3. 에러 핸들링이 포함된 게시물 조회
     * onStatus를 사용하여 4xx, 5xx 에러 발생 시 사용자 정의 예외를 던집니다.
     */
    public Post getPostWithCheck(Long id) {
        return restClient.get()
                .uri("/posts/{id}", id) // URI 변수 사용
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    // 4xx 에러(Client Error) 발생 시 로직 처리
                    throw new RuntimeException("게시글을 찾을 수 없습니다. (ID: " + id + ")");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    // 5xx 에러(Server Error) 발생 시 로직 처리
                    throw new RuntimeException("서버 오류가 발생했습니다.");
                })
                .body(Post.class);
    }

    /**
     * 4. 쿼리 파라미터를 사용한 필터링 조회 (GET /posts?userId=1)
     * uriBuilder를 사용하여 동적으로 쿼리 파라미터를 구성합니다.
     */
    public List<Post> getPostsByUserId(Long userId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/posts")
                        .queryParam("userId", userId) // ?userId={userId} 형태로 추가
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Post>>() {});
    }

    /**
     * 5. 새로운 게시글 생성 (POST /posts)
     * .body() 메서드에 객체를 넘기면 JSON으로 자동 직렬화됩니다.
     */
    public Post createPost(Post newPost) {
        return restClient.post() // HTTP POST 메서드 지정
                .uri("/posts")
                .body(newPost) // 요청 바디(Request Body) 설정
                .retrieve()
                .body(Post.class); // 생성된 결과물 수신
    }
}