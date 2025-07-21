package com.tbread.book.book.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateBookSeriesRequest(@NotNull(message = "시리즈 ID는 필수값입니다.") Long seriesId,
                                      @Pattern(regexp = "^\\d{13}$", message = "'-'를 제외한 13자리 숫자만 입력가능합니다.") @NotNull(message = "ISBN은 필수값입니다.") String isbn) {
}
