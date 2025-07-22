package com.tbread.book.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(@NotBlank(message = "아이디는 필수값입니다.") String username,
                            @NotBlank(message = "비밀번호는 필수값입니다.") String password,
                            @NotBlank(message = "본인인증 에러") String ci
        /* ci는 본인인증 서비스에서 넘어온다고 가정함 */) {
}
