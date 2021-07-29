package com.oognuyh.board.user.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oognuyh.board.user.model.User;
import com.oognuyh.board.user.repository.UserRepository;
import com.oognuyh.board.user.service.UserService;
import com.oognuyh.board.user.web.dto.SignupRequest;
import com.oognuyh.board.user.web.dto.UserUpdateRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class UserApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("회원가입")
    @Test
    void signup() throws Exception {
        // given
        SignupRequest signupRequest = SignupRequest.builder()
            .name("user")
            .email("user@example.com")
            .password("password")
            .passwordConfirmation("password")
            .phoneNumber("01000000000")
            .build();

        given(userService.signup(any(SignupRequest.class)))
            .willReturn(null);

        // when
        ResultActions resultActions = mvc
            .perform(post("/api/v1/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));

        // then
        resultActions
            .andExpect(status().isCreated());

        // verify
        verify(userService).signup(any(SignupRequest.class));
    }

    @DisplayName("잘못된 양식으로 회원가입")
    @Test
    void badRequestSignup() throws Exception {
        // given
        SignupRequest signupRequest = SignupRequest.builder()
            .name("name")
            .passwordConfirmation("password")
            .password("pass")
            .email("email")
            .phoneNumber("0124")
            .build();

        given(userService.signup(any(SignupRequest.class)))
            .willReturn(null);

        // when
        ResultActions resultActions = mvc
            .perform(post("/api/v1/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));

        // then
        resultActions
            .andExpect(status().isBadRequest());
    }

    @DisplayName("인증 토큰을 통한 회원 정보 조회")
    @WithMockUser(roles = "USER")
    @Test
    void findByAuthToken() throws Exception {
        // given
        String authToken = "auth";

        given(userService.findByAuthToken(any(String.class)))
            .willReturn(null);

        // when
        ResultActions resultActions = mvc
            .perform(post("/api/v1/user")
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
            .andExpect(status().isOk());
    }

    @DisplayName("권한 없는 사용자가 정보 변경")
    @WithAnonymousUser
    @Test
    void update() throws Exception {
        // given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
            .name("name")
            .password("password")
            .passwordConfirmation("password")
            .email("user@example.com")
            .phoneNumber("01000000000")
            .build();

        given(userService.update(any(UserUpdateRequest.class)))
            .willReturn(User.builder()
                .email(userUpdateRequest.getEmail())
                .name(userUpdateRequest.getName())
                .password(userUpdateRequest.getPassword())
                .password(userUpdateRequest.getPasswordConfirmation())
                .phoneNumber(userUpdateRequest.getPhoneNumber())
                .build());

        given(userRepository.findByEmail(any(String.class)))
            .willReturn(Optional.of(User.builder().build()));

        // when
        ResultActions resultActions = mvc
            .perform(put("/api/v1/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateRequest)));

        // then
        resultActions
            .andExpect(status().isForbidden());
    }
}