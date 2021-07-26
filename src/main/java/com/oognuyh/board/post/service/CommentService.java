package com.oognuyh.board.post.service;

import java.util.List;

import com.oognuyh.board.post.web.dto.CommentRequest;
import com.oognuyh.board.post.web.dto.CommentResponse;

public interface CommentService {
    
    List<CommentResponse> findCommentsByPostId(Long postId);
    CommentResponse save(CommentRequest commentRequest);
    CommentResponse update(CommentRequest commentRequest);
    void deleteById(Long id);
}