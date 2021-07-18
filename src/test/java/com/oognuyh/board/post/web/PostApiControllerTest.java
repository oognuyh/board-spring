package com.oognuyh.board.post.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oognuyh.board.post.service.PostService;
import com.oognuyh.board.post.web.dto.PostRequest;
import com.oognuyh.board.post.web.dto.PostResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(MockitoExtension.class)
public class PostApiControllerTest {
    private MockMvc mvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostApiController postApiController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void before() {
        mvc = MockMvcBuilders.standaloneSetup(postApiController).addFilter(new CharacterEncodingFilter("utf-8"))
                .alwaysDo(print()).build();
    }

    @DisplayName("모든 게시글 조회")
    @Test
    void findAll() throws Exception {
        // given
        List<PostResponse> fakes = Arrays.asList(
                new PostResponse(1L, "title 1", "content 1", "author 1", LocalDateTime.now(), LocalDateTime.now()),
                new PostResponse(2L, "title 2", "content 2", "author 2", LocalDateTime.now(), LocalDateTime.now()));

        given(postService.findAll()).willReturn(fakes);

        // when
        ResultActions resultActions = mvc.perform(get("/api/v1/post").accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(fakes.size()));

        // verify
        verify(postService).findAll();
    }

    @DisplayName("게시글 상세정보 조회")
    @Test
    void findById() throws Exception {
        // given
        PostResponse postResponse = PostResponse.builder().id(1L).title("title").content("content").author("author")
                .build();

        given(postService.findById(postResponse.getId())).willReturn(postResponse);

        // when
        ResultActions resultActions = mvc
                .perform(get("/api/v1/post/{id}", postResponse.getId()).accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(postResponse.getId()))
                .andExpect(jsonPath("$.title").value(postResponse.getTitle()))
                .andExpect(jsonPath("$.content").value(postResponse.getContent()))
                .andExpect(jsonPath("$.author").value(postResponse.getAuthor()));

        // verify
        verify(postService).findById(postResponse.getId());
    }

    @DisplayName("게시글 수정")
    @Test
    void update() throws Exception {
        // given
        PostRequest postRequest = PostRequest.builder().id(1L).title("new title").content("new content")
                .author("author").build();

        given(postService.update(any(PostRequest.class))).willReturn(postRequest.toEntity().toResponse());

        // when
        ResultActions resultActions = mvc.perform(put("/api/v1/post").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(postRequest)));

        // then
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(postRequest.getId()))
                .andExpect(jsonPath("$.title").value(postRequest.getTitle()))
                .andExpect(jsonPath("$.content").value(postRequest.getContent()))
                .andExpect(jsonPath("$.author").value(postRequest.getAuthor()));

        // verify
        verify(postService).update(any(PostRequest.class));
    }

    @DisplayName("게시글 삭제")
    @Test
    void deleteById() throws Exception {
        // given
        Long id = 1L;

        doNothing().when(postService).deleteById(id);

        // when
        ResultActions resultActions = mvc.perform(delete("/api/v1/post/{id}", id));

        // then
        resultActions
            .andExpect(status().isOk());

        // verify
        verify(postService).deleteById(id);
    }
}