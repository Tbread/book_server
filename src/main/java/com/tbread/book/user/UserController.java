package com.tbread.book.user;

import com.tbread.book.authentication.UserDetailsImpl;
import com.tbread.book.authentication.UserDetailsServiceImpl;
import com.tbread.book.authentication.jwt.JwtProcessor;
import com.tbread.book.common.dto.Result;
import com.tbread.book.user.dto.request.SignUpRequest;
import com.tbread.book.user.dto.request.UsernameAndPasswordRequest;
import com.tbread.book.user.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProcessor jwtProcessor;

    @PostMapping("signup")
    public ResponseEntity signup(@Valid @RequestBody SignUpRequest req,BindingResult br){
        if(br.hasErrors()){
            return new Result<>(br.getAllErrors().getFirst().getDefaultMessage(),HttpStatus.BAD_REQUEST,false).publish();
        }
        return userService.signup(req).publish();
    }

    @GetMapping("signout")
    public ResponseEntity signout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest req, HttpServletResponse res){
        if (Objects.isNull(userDetails)) {
            return new Result<>("로그인상태가 아닙니다.",HttpStatus.UNAUTHORIZED,false).publish();
        }
        res.addCookie(jwtProcessor.clearAccessCookie());
        res.addCookie(jwtProcessor.clearRefreshCookie());
        String token = jwtProcessor.extractToken(req).getRefreshToken();
        try {
            jwtProcessor.invalidateRefreshToken(token);
        } catch (JwtException | IllegalArgumentException ignored) {
        }
        return new Result<>(HttpStatus.OK,true).publish();
    }

}
