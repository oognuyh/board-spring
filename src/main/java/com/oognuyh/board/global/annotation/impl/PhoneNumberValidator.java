package com.oognuyh.board.global.annotation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.oognuyh.board.global.annotation.PhoneNumber;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return phoneNumber.matches("[0-9]+") && 9 < phoneNumber.length() && phoneNumber.length() < 12;
    }
    
}