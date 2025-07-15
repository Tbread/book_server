package com.tbread.book.book.entity;

import com.tbread.book.common.TimeStamp;
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

    @Column
    private String isbn;

    @Column
    private String isni;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClassificationNumber classificationNumber;

    @Column(nullable = false)
    private boolean series;

    @Column(nullable = false)
    private int ver;
    //동일 책중에서 몇번째권인지

    @Column(nullable = false)
    private boolean discard;

    //todo 시리즈용 엔티티 생성


}
