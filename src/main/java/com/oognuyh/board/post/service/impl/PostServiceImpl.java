package com.oognuyh.board.post.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.oognuyh.board.post.model.Post;
import com.oognuyh.board.post.repository.PostRepository;
import com.oognuyh.board.post.service.PostService;
import com.oognuyh.board.post.web.dto.PostRequest;
import com.oognuyh.board.post.web.dto.PostResponse;
import com.oognuyh.board.post.web.exception.PostDoesNotExistException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service("postService")
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    
    @Transactional(readOnly = true) 
    @Override
    public List<PostResponse> findAll() {
        return postRepository.findAll()
            .stream()
            .map(Post::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public PostResponse findById(Long id) {
        return postRepository.findById(id)
            .map(Post::toResponse)
            .orElseThrow(() -> new PostDoesNotExistException("해당 게시글은 존재하지 않습니다."));
    }

    @Transactional
    @Override
    public PostResponse save(PostRequest postRequest) {
        return postRepository.save(postRequest.toEntity())
            .toResponse();
    }

    @Transactional
    @Override
    public PostResponse update(PostRequest postRequest) {
        Post post = postRepository
            .findById(postRequest.getId())
            .orElseThrow(() -> new PostDoesNotExistException("해당 게시글은 존재하지 않습니다."));

        post.update(postRequest.getTitle(), postRequest.getContent());

        return post.toResponse();
    }


    @Transactional
    @Override
    public void deleteById(Long id) {
        postRepository.findById(id)
            .orElseThrow(() -> new PostDoesNotExistException("존재하지 않는 게시글을 삭제할 수 없습니다."));

        postRepository.deleteById(id);
    }
}