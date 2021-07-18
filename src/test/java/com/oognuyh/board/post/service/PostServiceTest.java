package com.oognuyh.board.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.oognuyh.board.post.model.Post;
import com.oognuyh.board.post.repository.PostRepository;
import com.oognuyh.board.post.service.impl.PostServiceImpl;
import com.oognuyh.board.post.web.dto.PostRequest;
import com.oognuyh.board.post.web.dto.PostResponse;
import com.oognuyh.board.post.web.exception.PostDoesNotExistException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @DisplayName("모든 게시글 조회")
    @Test
    void findAll() {
        // given 
        List<Post> fakes = Arrays.asList(
            new Post(1L, "title 1", "content 1", "author 1"),
            new Post(2L, "title 2", "content 2", "author 2")
        );

        given(postRepository.findAll())
            .willReturn(fakes);

        // when
        List<PostResponse> postResponses = postService.findAll();

        // then
        assertThat(postResponses.size()).isEqualTo(fakes.size());

        // verify
        verify(postRepository).findAll();
    }

    @DisplayName("게시글 아이디를 통한 조회")
    @Test
    void findById() {
        // given
        Long id = 1L;

        given(postRepository.findById(id))
            .willReturn(Optional.of(Post.builder()
                .id(1L)
                .title("title")
                .content("content")
                .author("author")
                .build()));

        // when
        PostResponse postResponse = postService.findById(id);

        // then
        assertNotNull(postResponse);

        // verify
        verify(postRepository).findById(id);
    }

    @DisplayName("게시글 저장")
    @Test
    void save() {
        // given
        PostRequest fake = PostRequest.builder()
            .id(1L)
            .title("title")
            .content("content")
            .author("author")
            .build();
        
        given(postRepository.save(any(Post.class)))
            .willReturn(fake.toEntity());

        // when
        PostResponse postResponse = postService.save(fake);

        // then
        assertNotNull(postResponse);

        // verify
        verify(postRepository).save(any(Post.class));
    }

    @DisplayName("게시글 수정")
    @Test
    void update() {
        // given
        PostRequest fake = PostRequest.builder()
            .id(1L)
            .title("new title")
            .content("new content")
            .author("author")
            .build();

        given(postRepository.findById(fake.getId()))
            .willReturn(Optional.of(Post.builder()
                .id(fake.getId())
                .title("title")
                .content("content")
                .author("author")
                .build()));

        // when
        PostResponse postResponse = postService.update(fake);

        // then
        assertAll(
            () -> assertThat(postResponse.getTitle()).isEqualTo(fake.getTitle()),
            () -> assertThat(postResponse.getContent()).isEqualTo(fake.getContent())
        );

        // verify
        verify(postRepository).findById(fake.getId());
    }

    @DisplayName("존재하지 않는 게시글 아이디를 통한 삭제")
    @Test
    void deleteById() {
        // given
        given(postRepository.findById(any(Long.class)))
            .willReturn(Optional.ofNullable(null));

        // when
        PostDoesNotExistException exception = assertThrows(
            PostDoesNotExistException.class, 
            () -> postService.deleteById(any(Long.class))
        );

        // then
        assertThat(exception.getMessage()).contains("존재하지 않는 게시글");

        //verify
        verify(postRepository).findById(any(Long.class));
    }
}