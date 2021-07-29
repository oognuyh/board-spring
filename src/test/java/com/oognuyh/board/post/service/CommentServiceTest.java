package com.oognuyh.board.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.oognuyh.board.post.model.Comment;
import com.oognuyh.board.post.model.Post;
import com.oognuyh.board.post.repository.CommentRepository;
import com.oognuyh.board.post.repository.PostRepository;
import com.oognuyh.board.post.service.impl.CommentServiceImpl;
import com.oognuyh.board.post.web.dto.CommentRequest;
import com.oognuyh.board.post.web.dto.CommentResponse;
import com.oognuyh.board.post.web.exception.CommentDoesNotExistException;
import com.oognuyh.board.post.web.exception.PostDoesNotExistException;
import com.oognuyh.board.user.model.User;
import com.oognuyh.board.user.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;
    
    @DisplayName("게시글 아이디를 통한 댓글 조회")
    @Test
    void findCommentsByPostId() {
        // given
        Long postId = 1L;
        List<Comment> fakes = Arrays.asList(Comment.builder()
            .post(Post.builder().id(postId).build())
            .user(User.builder().build())
            .content("content")
            .build());

        given(commentRepository.findCommentsByPostIdOrderByCreatedAtAsc(any(Long.class)))
            .willReturn(fakes);

        // when
        List<CommentResponse> commentResponses = commentService.findCommentsByPostId(postId);

        // then
        assertTrue(commentResponses.stream().allMatch(comment -> comment.getPostId() == postId));

        // verify
        verify(commentRepository).findCommentsByPostIdOrderByCreatedAtAsc(any(Long.class));
    }

    @DisplayName("댓글 수정")
    @Test
    void update() {
        // given
        Comment comment = Comment.builder()
            .id(1L)
            .content("content")
            .post(Post.builder().build())
            .user(User.builder().build())
            .build();
        CommentRequest commentRequest = CommentRequest.builder()
            .id(comment.getId())
            .content("new content")
            .build();

        given(commentRepository.findById(any(Long.class)))
            .willReturn(Optional.of(comment));

        // when
        CommentResponse commentResponse = commentService.update(commentRequest);

        // then
        assertThat(commentRequest.getContent()).isEqualTo(commentResponse.getContent());

        // verify
        verify(commentRepository).findById(any(Long.class));
    }

    @DisplayName("게시글이 존재하지 않는 댓글 저장")
    @Test
    void save() {
        // given
        CommentRequest commentRequest = CommentRequest.builder()
            .postId(1L)
            .build();

        given(postRepository.findById(any(Long.class)))
            .willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(
            Exception.class, 
            () -> commentService.save(commentRequest)
        );

        // then
        assertTrue(exception instanceof PostDoesNotExistException);

        // verify
        verify(postRepository).findById(any(Long.class));
    }

    @DisplayName("존재하지 않는 댓글을 삭제")
    @Test
    void delete() {
        // given
        given(commentRepository.findById(any(Long.class)))
            .willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(
            Exception.class,
            () -> commentService.deleteById(1L)
        );

        // then
        assertTrue(exception instanceof CommentDoesNotExistException);

        // verify
        verify(commentRepository).findById(any(Long.class));
    }
}