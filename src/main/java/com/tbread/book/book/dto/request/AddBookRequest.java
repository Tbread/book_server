package com.tbread.book.book.dto.request;

import com.tbread.book.annotation.SeriesRequestPair;
import com.tbread.book.book.entity.ClassificationNumber;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@SeriesRequestPair
public record AddBookRequest(@NotBlank(message = "제목은 필수값입니다.") String title,
                             @NotBlank(message = "저자명은 필수값입니다.") String author,
                             @NotBlank(message = "출판사는 필수값입니다.") String publisher,
                             @Pattern(regexp = "^\\d{13}$", message = "'-'를 제외한 13자리 숫자만 입력가능합니다.") @NotNull(message = "ISBN은 필수값입니다.") String isbn,
                             @Pattern(regexp = "^\\d{12}$", message = "'-'를 제외한 12자리 숫자만 입력가능합니다.") String isni,
                             @NotNull(message = "한국십진분류기호는 필수값입니다.") ClassificationNumber classificationNumber,
                             @NotNull(message = "시리즈여부는 필수값입니다.") Boolean isSeries,
                             Long seriesId,
                             @NotNull(message = "권수는 필수값입니다.") @Min(value = 1,message = "권수는 1 이상이여야 합니다.") Integer ea/* ea ->  추가될 책권수, isbn 으로 체크해서 마지막권수부터 +로 가져오기 */) {
}
