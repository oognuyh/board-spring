package com.oognuyh.board.post.service;

import java.util.List;

import com.oognuyh.board.post.web.dto.PostRequest;
import com.oognuyh.board.post.web.dto.PostResponse;

public interface PostService {
    
    List<PostResponse> findAll();
    PostResponse findById(Long id);
    PostResponse save(PostRequest postRequest);
    PostResponse update(PostRequest postRequest);
    void deleteById(Long id);
}