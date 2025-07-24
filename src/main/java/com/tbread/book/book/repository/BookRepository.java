package com.tbread.book.book.repository;

import com.tbread.book.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    Optional<Book> findFirstByIsbnOrderByVerDesc(String isbn);

    List<Book> findAll();
    //todo: 쿼리 dsl 에 조건들 이관하기
    List<Book> findAllByTitleContaining(String title);
    List<Book> findAllByAuthorContaining(String author);
    List<Book> findAllByPublisherContaining(String author);
    List<Book> findAllByIsbnContaining(String isbn);
    List<Book> findAllByIsniContaining(String isni);
    List<Book> findAllBySeries_SeriesNameContaining(String seriesName);
}
