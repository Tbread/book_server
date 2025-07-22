package com.tbread.book.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(@NotBlank String username,
                            @NotBlank String password,
                            @NotBlank String ci
        /* ci는 본인인증 서비스에서 넘어온다고 가정함 */) {
}
