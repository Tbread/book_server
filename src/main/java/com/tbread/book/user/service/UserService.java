package com.tbread.book.user.service;

import com.tbread.book.common.dto.Result;
import com.tbread.book.user.dto.request.SignUpRequest;
import com.tbread.book.user.entity.User;
import com.tbread.book.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
