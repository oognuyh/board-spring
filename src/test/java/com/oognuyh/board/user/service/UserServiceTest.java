package com.oognuyh.board.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.oognuyh.board.global.config.security.UserDetailsImpl;
import com.oognuyh.board.global.utils.JwtUtils;
import com.oognuyh.board.user.model.Roles;
import com.oognuyh.board.user.model.User;
import com.oognuyh.board.user.repository.UserRepository;
import com.oognuyh.board.user.service.impl.UserServiceImpl;
import com.oognuyh.board.user.web.dto.SignupRequest;
import com.oognuyh.board.user.web.dto.UserUpdateRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
 
    @DisplayName("회원가입 요청")
    @Test
    void signup() {
        // given
        SignupRequest request = SignupRequest.builder()
            .email("email@email.com")
            .name("name")
            .email("email")
            .password("password")
            .build();

        given(userRepository.save(any(User.class)))
            .willReturn(request.toEntity());
        
        // when
        User savedUser = userService.signup(request);

        // then
        assertThat(request.getEmail()).isEqualTo(savedUser.getEmail());

        // verify
        verify(userRepository).save(any(User.class));
    }

    @DisplayName("인증 토큰을 통한 사용자 조회")
    @Test
    void findByAuthToken() {
        // given
        User user = User.builder()
            .email("email")
            .role(Roles.ROLE_USER)
            .build();
        String authToken = JwtUtils.PREFIX + JwtUtils.generateToken(UserDetailsImpl.of(user));
        
        given(userRepository.findByEmail(any(String.class)))
            .willReturn(Optional.of(user));

        // when
        UserDetailsImpl userDetails = userService.findByAuthToken(authToken);

        // then
        assertThat(userDetails.getEmail()).isEqualTo(user.getEmail());

        // verify
        verify(userRepository).findByEmail(any(String.class));
    }

    @DisplayName("존재하지 않는 사용자 정보 수정")
    @Test
    void update() {
        // given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
            .email("none")
            .build();

        given(userRepository.findByEmail(any(String.class)))
            .willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(
            Exception.class,
            () -> userService.update(userUpdateRequest)
        );

        // then
        assertTrue(exception instanceof UsernameNotFoundException);

        // verify
        verify(userRepository).findByEmail(any(String.class));
    }
}