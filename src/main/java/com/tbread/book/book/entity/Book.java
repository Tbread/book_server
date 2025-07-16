package com.tbread.book.book.entity;

import com.tbread.book.common.TimeStamp;
import com.tbread.book.series.entity.Series;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Book extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    private String isbn;

    private String isni;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClassificationNumber classificationNumber;

    @Column(nullable = false)
    private boolean isSeries;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Series series;

    @Column(nullable = false)
    private int ver;
    //동일 책중에서 몇번째권인지

    @Column(nullable = false)
    private boolean discard;

    private Book(BookBuilder builder) {
        this.title = builder.title;
        this.author = builder.author;
        this.publisher = builder.publisher;
        this.isbn = builder.isbn;
        this.isni = builder.isni;
        this.classificationNumber = builder.classificationNumber;
        this.isSeries = builder.isSeries;
        this.series = builder.series;
        this.ver = builder.ver;
        this.discard = builder.discard;
    }

    public static class BookBuilder {
        private String title;
        private String author;
        private String publisher;
        private ClassificationNumber classificationNumber;
        private boolean isSeries;
        private int ver;
        private boolean discard;

        private String isbn;
        private String isni;
        private Series series;

        public BookBuilder(String title,
                           String author,
                           String publisher,
                           ClassificationNumber classificationNumber,
                           boolean isSeries,
                           int ver) {
            this.title = title;
            this.author = author;
            this.publisher = publisher;
            this.classificationNumber = classificationNumber;
            this.isSeries = isSeries;
            this.ver = ver;
            this.discard = false;
        }

        public BookBuilder setIsbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public BookBuilder setIsni(String isni) {
            this.isni = isni;
            return this;
        }

        public BookBuilder setSeries(Series series) {
            this.series = series;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }



}
