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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oognuyh.board.post.service.CommentService;
import com.oognuyh.board.post.web.dto.CommentRequest;
import com.oognuyh.board.post.web.dto.CommentResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("해당 게시글 댓글 조회")
    @Test
    void findCommentsByPostId() throws Exception {
        // given
        List<CommentResponse> fakes = Arrays.asList(
                new CommentResponse(1L, "content", 1L, "author", 1L, false, LocalDateTime.now(), LocalDateTime.now()));

        given(commentService.findCommentsByPostId(any(Long.class))).willReturn(fakes);

        // when
        ResultActions resultActions = mvc
            .perform(get("/api/v1/comment?postId=1")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(fakes.size()));

        // verify
        verify(commentService).findCommentsByPostId(any(Long.class));
    }

    
    @DisplayName("익명 사용자가 댓글 저장")
    @WithAnonymousUser
    @Test
    void save() throws JsonProcessingException, Exception {
        // given 
        CommentRequest commentRequest = CommentRequest.builder()
            .content("content")
            .userId(1L)
            .postId(1L)
            .build();

        given(commentService.save(any(CommentRequest.class)))
            .willReturn(CommentResponse.builder()
                .authorId(commentRequest.getUserId())
                .content(commentRequest.getContent())
                .postId(commentRequest.getPostId())
                .build());

        // when
        ResultActions resultActions = mvc
            .perform(post("/api/v1/comment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)));

        // then
        resultActions
            .andExpect(status().isForbidden());
    }

    @DisplayName("내용없이 댓글 수정")
    @WithMockUser(roles = "USER")
    @Test
    void update() throws JsonProcessingException, Exception {
        // given 
        CommentRequest commentRequest = CommentRequest.builder()
            .userId(1L)
            .postId(1L)
            .build();

        given(commentService.update(any(CommentRequest.class)))
            .willReturn(CommentResponse.builder()
                .authorId(commentRequest.getUserId())
                .content(commentRequest.getContent())
                .postId(commentRequest.getPostId())
                .build());

        // when
        ResultActions resultActions = mvc.perform(put("/api/v1/comment")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commentRequest)));

        // then
        resultActions
            .andExpect(status().isBadRequest());
    }

    @DisplayName("댓글 아이디를 통한 댓글 삭제")
    @WithMockUser(roles = "USER")
    @Test
    void deleteById() throws Exception {
        // given 
        Long id = 1L;

        doNothing().when(commentService).deleteById(id);

        // when
        ResultActions resultActions = mvc.perform(delete("/api/v1/comment/{id}", id));

        // then
        resultActions
            .andExpect(status().isOk());

        // verify
        verify(commentService).deleteById(id);
    }
}