package com.oognuyh.board.global.annotation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.oognuyh.board.global.annotation.Password;
import com.oognuyh.board.user.repository.UserRepository;
import com.oognuyh.board.user.web.dto.UserUpdateRequest;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<Password, UserUpdateRequest> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(UserUpdateRequest request, ConstraintValidatorContext context) {
        String password = request.getPassword();
        String existingPassword = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저의 비밀번호를 변경할 수 없습니다"))
            .getPassword();
        
        context.disableDefaultConstraintViolation();

        if (StringUtils.hasText(password)) {
            if (passwordEncoder.matches(password, existingPassword)) {
                return true;
            } else {
                if (8 <= password.length() && password.length() <= 20) {
                    return true;
                } else {
                    context.buildConstraintViolationWithTemplate("비밀번호는 8자리 이상 20자리 이하로 입력하세요")
                        .addPropertyNode("password")
                        .addConstraintViolation();
                }
            }
        } else {
            context.buildConstraintViolationWithTemplate("비밀번호를 입력하세요")
                .addPropertyNode("password")
                .addConstraintViolation();
        }

        return false;
    }
    
}