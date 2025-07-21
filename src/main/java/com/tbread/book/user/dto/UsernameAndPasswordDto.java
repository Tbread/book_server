package com.tbread.book.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UsernameAndPasswordDto(@NotBlank(message = "아이디는 필수값입니다.") String username,
                                     @NotBlank(message = "패스워드는 필수값입니다.") String password) {
}
