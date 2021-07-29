package com.oognuyh.board.user.web.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;

import com.oognuyh.board.global.annotation.Password;
import com.oognuyh.board.global.annotation.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@Password
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    private String password;

    private String passwordConfirmation;
    
    @AssertTrue(message = "비밀번호가 일치하지 않습니다")
    public boolean isPasswordConfirmation() {
        return password.equals(passwordConfirmation);
    }

    @NotBlank(message = "이메일을 입력하세요")
    private String email;

    @NotBlank(message = "전화번호를 입력하세요")
    @PhoneNumber
    private String phoneNumber;
}