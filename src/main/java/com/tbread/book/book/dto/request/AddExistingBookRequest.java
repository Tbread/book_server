package com.tbread.book.book.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AddExistingBookRequest(
        @Pattern(regexp = "^\\d{13}$", message = "'-'를 제외한 13자리 숫자만 입력가능합니다.") @NotNull(message = "ISBN은 필수값입니다.") String isbn,
        @NotNull(message = "권수는 필수값입니다.") @Min(value = 1, message = "권수는 1 이상이여야 합니다.") Integer ea) {
}
