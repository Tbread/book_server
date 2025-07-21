package com.tbread.book.authentication;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbread.book.authentication.jwt.JwtProcessor;
import com.tbread.book.common.dto.Result;
import com.tbread.book.user.dto.request.UsernameAndPasswordRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class RestfulLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtProcessor jwtProcessor;

    public RestfulLoginAuthenticationFilter(AuthenticationManager authenticationManager,JwtProcessor jwtProcessor) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtProcessor = jwtProcessor;
        setFilterProcessesUrl("/api/v1/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            UsernameAndPasswordRequest usernameAndPasswordRequest = objectMapper.readValue(request.getInputStream(), UsernameAndPasswordRequest.class);

            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(usernameAndPasswordRequest.username(), usernameAndPasswordRequest.password());

            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new AuthenticationServiceException("로그인 요청 파싱 실패", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //todo: jwt 리턴로직 작성
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Result res = new Result<>("로그인에 실패했습니다.", HttpStatus.BAD_REQUEST,false);
        response.getWriter().write(objectMapper.writeValueAsString(res));
    }
}
