package com.tbread.book.book.repository;

import com.tbread.book.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    Optional<Book> findFirstByIsbnOrderByVerDesc(String isbn);
}
