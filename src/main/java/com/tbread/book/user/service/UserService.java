package com.tbread.book.user.service;

import com.tbread.book.common.dto.Result;
import com.tbread.book.external.authentication.PersonalAuthenticationService;
import com.tbread.book.user.dto.request.ResetPasswordRequest;
import com.tbread.book.user.dto.request.SignUpRequest;
import com.tbread.book.user.entity.User;
import com.tbread.book.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonalAuthenticationService personalAuthenticationService;

    @Transactional
    public Result signup(SignUpRequest req){
        if (userRepository.existsByUsername(req.username())){
            return new Result("이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST,false);
        }
        if (userRepository.existsByCi(req.ci())){
            return new Result("이미 가입된 명의입니다.",HttpStatus.BAD_REQUEST,false);
        }
        User user = new User.UserBuilder(req.username(),passwordEncoder.encode(req.password()), req.ci()).build();
        userRepository.save(user);
        return new Result<>(HttpStatus.OK,req.username(),true);
    }

    public Result usernameCheck(String username){
        if (Objects.isNull(username) || username.isBlank()){
            return new Result<>("아이디는 필수값입니다.",HttpStatus.BAD_REQUEST,false);
        }
        if (userRepository.existsByUsername(username)){
            return new Result("이미 존재하는 아이디입니다.",HttpStatus.BAD_REQUEST,false);
        }
        return new Result<>("사용가능한 아이디입니다.",HttpStatus.OK,username,true);
    }

    @Transactional
    public Result resetPassword(ResetPasswordRequest req){
        Optional<User> optionalUser = userRepository.findByUsername(req.username());
        if (optionalUser.isEmpty()) {
            return new Result("존재하지 않는 아이디입니다.", HttpStatus.BAD_REQUEST, false);
        }
        if (!personalAuthenticationService.isMatchCi(req.username(), req.ci())){
            return new Result("본인인증 정보가 일치하지 않습니다.",HttpStatus.BAD_REQUEST,false);
        }
        User user = optionalUser.get();
        user.updatePassword(passwordEncoder.encode(req.password()));
        userRepository.save(user);
        return new Result<>(HttpStatus.OK,req.username(),true);
    }
}
