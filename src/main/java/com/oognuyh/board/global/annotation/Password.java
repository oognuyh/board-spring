package com.oognuyh.board.global.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.oognuyh.board.global.annotation.impl.PasswordValidator;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default "비밀번호가 올바르지 않습니다.";

    Class<?>[] groups() default {};
  
    Class<? extends Payload>[] payload() default {};
}