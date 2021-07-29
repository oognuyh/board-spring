package com.oognuyh.board.user.web.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.oognuyh.board.global.annotation.PhoneNumber;
import com.oognuyh.board.global.annotation.UniqueEmail;
import com.oognuyh.board.user.model.Roles;
import com.oognuyh.board.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @Size(min = 8, max = 20, message = "비밀번호는 8자리 이상 20자리 이하로 입력하세요")
    private String password;

    private String passwordConfirmation;
    
    @AssertTrue(message = "비밀번호가 일치하지 않습니다")
    public boolean isPasswordConfirmation() {
        return password.equals(passwordConfirmation);
    }

    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "이메일이 양식에 맞지 않습니다")
    @UniqueEmail
    private String email;

    @NotBlank(message = "전화번호를 입력하세요")
    @PhoneNumber
    private String phoneNumber;

    @Builder.Default
    private Roles role = Roles.ROLE_USER;
    
    public User toEntity() {
        return User.builder()
            .name(name)
            .password(password)
            .email(email)
            .phoneNumber(phoneNumber)
            .role(role)
            .build();
    }
}