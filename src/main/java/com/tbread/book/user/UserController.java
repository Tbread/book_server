package com.tbread.book.user;

import com.tbread.book.authentication.UserDetailsServiceImpl;
import com.tbread.book.common.dto.Result;
import com.tbread.book.user.dto.request.UsernameAndPasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

//    private final UserDetailsServiceImpl userDetailsService;

//    @PostMapping("login")
//    public ResponseEntity login(@Valid @RequestBody UsernameAndPasswordRequest req, BindingResult br){
//        if (br.hasErrors()) {
//            return new Result<>(br.getAllErrors().getFirst().getDefaultMessage(), HttpStatus.BAD_REQUEST,false).publish();
//        }
//        return userDetailsService.login(req).publish();
//    }
}
