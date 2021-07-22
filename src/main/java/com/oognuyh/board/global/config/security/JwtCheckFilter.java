package com.oognuyh.board.global.config.security;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oognuyh.board.global.utils.JwtUtils;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtCheckFilter extends BasicAuthenticationFilter {
    private UserDetailsServiceImpl userDetailsService;

    public JwtCheckFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        super(authenticationManager);
        
        this.userDetailsService = userDetailsService;
    }
    

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationToken == null || !authorizationToken.startsWith(JwtUtils.PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationToken.substring(JwtUtils.PREFIX.length());
        VerifiedResult result = JwtUtils.verify(token);

        if (result.isSuccessful()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(result.getEmail());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getEmail(), null, userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        } else {
            throw new AuthenticationException("토큰이 유효하지 않습니다.");
        }
    }
}