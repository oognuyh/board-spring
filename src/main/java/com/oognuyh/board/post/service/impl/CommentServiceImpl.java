package com.oognuyh.board.post.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.oognuyh.board.post.model.Comment;
import com.oognuyh.board.post.repository.CommentRepository;
import com.oognuyh.board.post.repository.PostRepository;
import com.oognuyh.board.post.service.CommentService;
import com.oognuyh.board.post.web.dto.CommentRequest;
import com.oognuyh.board.post.web.dto.CommentResponse;
import com.oognuyh.board.post.web.exception.CommentDoesNotExistException;
import com.oognuyh.board.post.web.exception.PostDoesNotExistException;
import com.oognuyh.board.user.repository.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service("commentService")
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CommentResponse> findCommentsByPostId(Long postId) {
        return commentRepository.findCommentsByPostIdOrderByCreatedAtAsc(postId).stream()
            .map(Comment::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentResponse update(CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentRequest.getId())
            .orElseThrow(() -> new CommentDoesNotExistException("해당 댓글은 존재하지 않습니다"));

        comment.update(commentRequest.getContent());

        return comment.toResponse();
    }

    @Transactional
    @Override
    public CommentResponse save(CommentRequest commentRequest) {
        Comment comment = commentRequest.toEntity();

        comment.setPost(postRepository.findById(commentRequest.getPostId())
            .orElseThrow(() -> new PostDoesNotExistException("해당 게시글이 존재하지 않습니다")));

        comment.setUser(userRepository.findById(commentRequest.getUserId())
            .orElseThrow(() -> new UsernameNotFoundException("해당 사용자는 존재하지 않습니다")));
        
        return commentRepository.save(comment)
            .toResponse();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        commentRepository.findById(id)
            .orElseThrow(() -> new CommentDoesNotExistException("존재하지 않는 댓글을 삭제할 수 없습니다"));

        commentRepository.deleteById(id);
    }
}