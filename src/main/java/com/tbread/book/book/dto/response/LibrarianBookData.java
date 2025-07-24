package com.tbread.book.book.dto.response;

import com.tbread.book.book.entity.Book;
import com.tbread.book.book.entity.enums.ClassificationNumber;
import lombok.Getter;

@Getter
public class LibrarianBookData {
    private long bookId;
    private String title;
    private String author;
    private String publisher;
    private ClassificationNumber classificationNumber;
    private boolean isSeries;
    private int ver;
    private boolean discard;
    private String isbn;
    private String isni;
    private Long seriesId;
    private String seriesName;

    public LibrarianBookData(Book book) {
        this.bookId = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.classificationNumber = book.getClassificationNumber();
        this.isSeries = book.isSeries();
        this.ver = book.getVer();
        this.isbn = book.getIsbn();
        this.isni = book.getIsni();
        this.discard = book.isDiscard();
        this.seriesId = book.getSeries() == null ? null : book.getSeries().getId();
        this.seriesName = book.getSeries() == null ? null : book.getSeries().getSeriesName();
    }
}
