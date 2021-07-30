package com.oognuyh.board.global.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oognuyh.board.global.utils.JwtUtils;
import com.oognuyh.board.user.web.dto.LoginRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.SneakyThrows;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private ObjectMapper objectMapper = new ObjectMapper();
    private UserDetailsServiceImpl userDetailsService;

    public JwtLoginFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;

        setFilterProcessesUrl("/api/v1/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

        if (loginRequest.getRefreshToken() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword(), null
            );

            return getAuthenticationManager().authenticate(token);
        } else {
            VerifiedResult verifiedResult = JwtUtils.verify(loginRequest.getRefreshToken());

            if (verifiedResult.isSuccessful()) {
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(verifiedResult.getEmail());

                return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
            } else {
                throw new TokenExpiredException("만료된 토큰입니다");
            }
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {  

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        response.setHeader(JwtUtils.AUTH_TOKEN_HEADER, JwtUtils.PREFIX + JwtUtils.generateToken(userDetails));
        response.setHeader(JwtUtils.REFRESH_TOKEN_HEADER, JwtUtils.refreshToken(userDetails));

        response.getOutputStream().write(objectMapper.writeValueAsBytes(userDetails));

    }
}