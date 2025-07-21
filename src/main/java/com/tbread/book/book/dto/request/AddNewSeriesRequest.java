package com.tbread.book.book.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddNewSeriesRequest(@NotBlank(message = "시리즈명은 필수값입니다.") String seriesName) {
}
