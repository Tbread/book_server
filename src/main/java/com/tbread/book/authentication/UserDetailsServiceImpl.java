package com.tbread.book.authentication;

import com.tbread.book.authentication.jwt.JwtProcessor;
import com.tbread.book.common.TokenPackage;
import com.tbread.book.common.dto.Result;
import com.tbread.book.user.dto.request.UsernameAndPasswordRequest;
import com.tbread.book.user.dto.response.LoginResponse;
import com.tbread.book.user.entity.User;
import com.tbread.book.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProcessor jwtProcessor;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지않는 아이디입니다."));
        return new UserDetailsImpl(user);
    }

    public Result<LoginResponse> login(UsernameAndPasswordRequest req) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(req.username(), req.password());
            Authentication auth = authenticationManager.authenticate(authToken);
            String accessToken = jwtProcessor.createToken(auth.getName(), JwtProcessor.JwtType.ACCESS);
            String refreshToken = jwtProcessor.createToken(auth.getName(), JwtProcessor.JwtType.REFRESH);
            LoginResponse res = new LoginResponse(refreshToken, accessToken);
            return new Result<>(HttpStatus.OK, res, true);
        } catch (AuthenticationException e) {
            return new Result<>("올바르지않은 유저 정보입니다.", HttpStatus.BAD_REQUEST, false);
        }
    }
}
