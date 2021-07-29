package com.oognuyh.board.user.service.impl;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.oognuyh.board.global.config.security.UserDetailsImpl;
import com.oognuyh.board.global.config.security.VerifiedResult;
import com.oognuyh.board.global.utils.JwtUtils;
import com.oognuyh.board.user.model.User;
import com.oognuyh.board.user.repository.UserRepository;
import com.oognuyh.board.user.service.UserService;
import com.oognuyh.board.user.web.dto.SignupRequest;
import com.oognuyh.board.user.web.dto.UserUpdateRequest;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User signup(SignupRequest request) {
        User user = request.toEntity();

        user.encodePassword(passwordEncoder);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetailsImpl findByAuthToken(String authToken) {
        VerifiedResult verifiedResult = JwtUtils.verify(authToken.substring(JwtUtils.PREFIX.length()));

        if (verifiedResult.isSuccessful()) {
            User user = userRepository.findByEmail(verifiedResult.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("잘못된 사용자입니다"));

            return UserDetailsImpl.of(user);
        } else {
            throw new TokenExpiredException("만료된 토큰입니다");
        }
    }

    @Transactional
    @Override
    public User update(UserUpdateRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자를 변경할 수 없습니다"));

        user.update(request);
        user.encodePassword(passwordEncoder);

        return user;
    }
}