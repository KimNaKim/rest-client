package com.example.restclient_study.post;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
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
}