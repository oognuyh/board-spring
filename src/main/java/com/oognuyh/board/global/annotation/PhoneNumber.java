package com.oognuyh.board.global.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.oognuyh.board.global.annotation.impl.PhoneNumberValidator;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default "전화번호가 양식에 맞지 않습니다.";

    Class<?>[] groups() default {};
  
    Class<? extends Payload>[] payload() default {};
}