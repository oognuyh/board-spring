package com.oognuyh.board.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.oognuyh.board.user.model.Roles;
import com.oognuyh.board.user.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void save() {
        // given
        User user = User.builder()
            .name("name")
            .password("password")
            .phoneNumber("phoneNumber")
            .role(Roles.ROLE_USER)
            .build();

        // when
        savedUser = userRepository.save(user);

        // then
        assertAll(
            () -> assertThat(savedUser.getName()).isEqualTo(user.getName()),
            () -> assertThat(savedUser.getPhoneNumber()).isEqualTo(user.getPhoneNumber())
        );
    }

    @DisplayName("이메일로 사용자 조회")
    @Test
    void findByEmail() {
        // when
        User user = userRepository.findByEmail(savedUser.getEmail())
            .orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
            () -> assertThat(user.getId()).isEqualTo(savedUser.getId()),
            () -> assertThat(user.getName()).isEqualTo(savedUser.getName())
        );
    }

    @DisplayName("존재하는 이메일 확인")
    @Test
    void existsByEmail() {
        // when
        boolean isAreadyIn = userRepository.existsByEmail(savedUser.getEmail());

        // then
        assertTrue(isAreadyIn);
    }
}