package com.oognuyh.board.post.service;

import com.oognuyh.board.post.web.dto.PostRequest;
import com.oognuyh.board.post.web.dto.PostResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    
    Page<PostResponse> findPosts(Pageable pageable);
    PostResponse findById(Long id);
    PostResponse save(PostRequest postRequest);
    PostResponse update(PostRequest postRequest);
    void deleteById(Long id);
}