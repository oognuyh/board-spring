package com.oognuyh.board.user.service;

import com.oognuyh.board.global.config.security.UserDetailsImpl;
import com.oognuyh.board.user.model.User;
import com.oognuyh.board.user.web.dto.SignupRequest;
import com.oognuyh.board.user.web.dto.UserUpdateRequest;

public interface UserService {
    
    User signup(SignupRequest request);
    UserDetailsImpl findByAuthToken(String authToken);
    User update(UserUpdateRequest request);
}