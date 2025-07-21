package com.tbread.book.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UsernameAndPasswordRequest(@NotBlank(message = "아이디는 필수값입니다.") String username,
                                     @NotBlank(message = "패스워드는 필수값입니다.") String password) {
}
