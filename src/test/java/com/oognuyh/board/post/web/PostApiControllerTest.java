package com.oognuyh.board.post.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oognuyh.board.post.service.PostService;
import com.oognuyh.board.post.web.dto.PostRequest;
import com.oognuyh.board.post.web.dto.PostResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class PostApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostService postService;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("게시글 페이징 조회")
    @Test
    void getPosts() throws Exception {
        // given
        List<PostResponse> fakes = Arrays.asList(
            new PostResponse(1L, "title 1", "content 1", "author 1", 1L, 0, LocalDateTime.now(), LocalDateTime.now()),
            new PostResponse(2L, "title 2", "content 2", "author 2", 2L, 0, LocalDateTime.now(), LocalDateTime.now())
        );

        given(postService.findPosts(any())).willReturn(new PageImpl<>(fakes));

        // when
        ResultActions resultActions = mvc
            .perform(get("/api/v1/post")
            .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()")
            .value(fakes.size()));

        // verify
        verify(postService).findPosts(any());
    }

    @DisplayName("게시글 상세정보 조회")
    @Test
    void findById() throws Exception {
        // given
        PostResponse postResponse = PostResponse.builder()
            .id(1L)
            .title("title")
            .content("content")
            .author("author")
            .build();

        given(postService.findById(postResponse.getId())).willReturn(postResponse);

        // when
        ResultActions resultActions = mvc
            .perform(get("/api/v1/post/{id}", postResponse.getId())
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(postResponse.getId()))
            .andExpect(jsonPath("$.title").value(postResponse.getTitle()))
            .andExpect(jsonPath("$.content").value(postResponse.getContent()))
            .andExpect(jsonPath("$.author").value(postResponse.getAuthor()));

        // verify
        verify(postService).findById(postResponse.getId());
    }

    @DisplayName("잘못된 게시글 저장")
    @WithMockUser(roles = "USER")
    @Test
    void save() throws Exception {
        // given
        PostRequest postRequest = PostRequest.builder()
            .build();

        // when
        ResultActions resultActions = mvc
            .perform(post("/api/v1/post")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest)));

        // then
        resultActions
            .andExpect(status().isBadRequest());
    }

    @DisplayName("게시글 수정")
    @WithMockUser(roles = "USER")
    @Test
    void update() throws Exception {
        // given
        PostRequest postRequest = PostRequest.builder().id(1L).title("new title").content("new content")
                .userId(1L).build();

        given(postService.update(any(PostRequest.class))).willReturn(PostResponse.builder()
                .id(1L)
                .title("new title")
                .content("new content")
                .author("author")
                .userId(1L)
                .build());

        // when
        ResultActions resultActions = mvc
            .perform(put("/api/v1/post")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest)));

        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(postRequest.getId()))
            .andExpect(jsonPath("$.title").value(postRequest.getTitle()))
            .andExpect(jsonPath("$.content").value(postRequest.getContent()));

        // verify
        verify(postService).update(any(PostRequest.class));
    }

    @DisplayName("권한없는 사용자가 게시글 삭제")
    @WithAnonymousUser
    @Test
    void deleteById() throws Exception {
        // given
        Long id = 1L;

        doNothing().when(postService).deleteById(id);

        // when
        ResultActions resultActions = mvc.perform(delete("/api/v1/post/{id}", id));

        // then
        resultActions
            .andExpect(status().isForbidden());
    }
}