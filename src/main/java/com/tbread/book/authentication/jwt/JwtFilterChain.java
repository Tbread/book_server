package com.tbread.book.authentication.jwt;

import com.tbread.book.common.TokenPackage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
public class JwtFilterChain extends GenericFilterBean {
    private final JwtProcessor jwtProcessor;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;
        TokenPackage tokenPackage = jwtProcessor.extractToken(httpReq);
        if (Objects.nonNull(tokenPackage.getRefreshToken()) && Objects.nonNull(tokenPackage.getAccessToken())) {
            if (jwtProcessor.isValidate(tokenPackage.getAccessToken())) {
                //액세스 토큰 비만료
                Authentication authentication = jwtProcessor.getAuthentication(tokenPackage.getAccessToken());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                //액세스 토큰 만료
                if (jwtProcessor.isValidate(tokenPackage.getRefreshToken()) && !jwtProcessor.isInvalidatedToken(tokenPackage)) {
                    //리프레시 토큰 비만료
                    String newAccessToken = jwtProcessor.createToken(jwtProcessor.getClaims(tokenPackage.getRefreshToken()).getPayload().getSubject(), JwtProcessor.JwtType.ACCESS);
                    httpRes.addCookie(jwtProcessor.setJwtCookie(newAccessToken, JwtProcessor.JwtType.ACCESS));
                    Authentication authentication = jwtProcessor.getAuthentication(newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    httpRes.addCookie(jwtProcessor.clearRefreshCookie());
                    httpRes.addCookie(jwtProcessor.clearAccessCookie());
                }
            }
        }
        //todo? : c#에서 체크해보니 set-cookie 헤더로 자동설정이 힘들것같음, 그냥 헤더로 바꾸는게 나을지도?
        chain.doFilter(request, response);
    }
}
