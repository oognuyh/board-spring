package com.oognuyh.board.user.web;

import javax.validation.Valid;

import com.oognuyh.board.global.config.security.UserDetailsImpl;
import com.oognuyh.board.user.service.UserService;
import com.oognuyh.board.user.web.dto.SignupRequest;
import com.oognuyh.board.user.web.dto.UserUpdateRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserApiController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        userService.signup(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/user")
    public ResponseEntity<UserDetailsImpl> findByAuthToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        return new ResponseEntity<>(userService.findByAuthToken(authToken), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/user")
    public ResponseEntity<?> update(@Valid @RequestBody UserUpdateRequest request) {
        userService.update(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}