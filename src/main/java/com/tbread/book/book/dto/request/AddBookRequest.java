package com.tbread.book.book.dto.request;

import com.tbread.book.book.entity.ClassificationNumber;

public record AddBookRequest(String title, String author, String publisher, String isbn, String isni,
                             ClassificationNumber classificationNumber, boolean isSeries, long seriesId,
                             int ea/* ea ->  추가될 책권수, isbn 으로 체크해서 마지막권수부터 +로 가져오기 */) {
}
