package com.oognuyh.board.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.oognuyh.board.post.model.Comment;
import com.oognuyh.board.post.model.Post;
import com.oognuyh.board.post.repository.PostRepository;
import com.oognuyh.board.post.service.impl.PostServiceImpl;
import com.oognuyh.board.post.web.dto.PostRequest;
import com.oognuyh.board.post.web.dto.PostResponse;
import com.oognuyh.board.post.web.exception.PostDoesNotExistException;
import com.oognuyh.board.user.model.Roles;
import com.oognuyh.board.user.model.User;
import com.oognuyh.board.user.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @DisplayName("게시글 페이징 조회")
    @Test
    void find() {
        // given 
        List<Post> fakes = Arrays.asList(
            new Post(1L, "title 1", "content 1", new User(1L, "user", "password", "user@email.com", "01000000000", Roles.ROLE_USER), new ArrayList<Comment>()),
            new Post(2L, "title 2", "content 2", new User(1L, "user", "password", "user@email.com", "01000000000", Roles.ROLE_USER), new ArrayList<Comment>())
        );
        int page = 0;
        int size = fakes.size();

        given(postRepository.findAll(any(Pageable.class)))
            .willReturn(new PageImpl<>(fakes, PageRequest.of(page, size), fakes.size()));

        // when
        Page<PostResponse> posts = postService.findPosts(PageRequest.of(page, size));

        // then
        assertThat(posts.getSize()).isEqualTo(fakes.size());

        // verify
        verify(postRepository).findAll(any(Pageable.class));
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
                .user(User.builder()
                    .name("name")
                    .password("password")
                    .email("email@email.com")
                    .phoneNumber("01000000000")
                    .role(Roles.ROLE_USER)
                    .build())
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
            .userId(1L)
            .build();
        
        User user = User.builder().id(1L).name("name").password("password").phoneNumber("01000000000").role(Roles.ROLE_USER).build();
        
        given(postRepository.save(any(Post.class)))
            .willReturn(Post.builder()
                .id(1L)
                .title("title")
                .content("content")
                .user(user)
                .build());

        given(userRepository.findById(any(Long.class)))
            .willReturn(Optional.of(user));

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
            .userId(1L)
            .build();

        given(postRepository.findById(fake.getId()))
            .willReturn(Optional.of(Post.builder()
                .id(fake.getId())
                .title("title")
                .content("content")
                .user(User.builder()
                    .id(1L)
                    .name("name")
                    .password("password")
                    .email("email@email.com")
                    .phoneNumber("01000000000")
                    .role(Roles.ROLE_USER)
                    .build())
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
        Exception exception = assertThrows(
            Exception.class, 
            () -> postService.deleteById(any(Long.class))
        );

        // then
        assertTrue(exception instanceof PostDoesNotExistException);

        //verify
        verify(postRepository).findById(any(Long.class));
    }
}