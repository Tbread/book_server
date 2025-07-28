package com.tbread.book.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(@NotBlank(message = "아이디는 필수값입니다.") String username,
                                   @NotBlank(message = "비밀번호는 필수값입니다.")String password,
                                   @NotBlank(message = "ci는 필수값입니다.") String ci) {
}
