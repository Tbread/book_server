package com.tbread.book.book.service;


import com.tbread.book.book.dto.request.AddBookRequest;
import com.tbread.book.book.dto.request.AddExistingBookRequest;
import com.tbread.book.book.dto.request.AddNewSeriesRequest;
import com.tbread.book.book.dto.request.UpdateBookSeriesRequest;
import com.tbread.book.book.dto.response.LibrarianBookData;
import com.tbread.book.book.entity.Book;
import com.tbread.book.book.entity.Series;
import com.tbread.book.book.entity.enums.BookSearchCondition;
import com.tbread.book.book.repository.BookRepository;
import com.tbread.book.book.repository.SeriesRepository;
import com.tbread.book.common.dto.Result;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final SeriesRepository seriesRepository;

    @Transactional
    public Result<?> addNewBooks(AddBookRequest req) {
        if (bookRepository.existsByIsbn(req.isbn())) {
            return new Result<>("이미 동일한 ISBN이 존재합니다. 기존 책 추가 기능을 이용해주세요.", HttpStatus.BAD_REQUEST, false);
        }
        Series series = null;
        if (req.isSeries()) {
            Optional<Series> optionalSeries = seriesRepository.findById(req.seriesId());
            if (optionalSeries.isEmpty()) {
                return new Result<>("올바르지않은 시리즈값입니다.", HttpStatus.BAD_REQUEST, false);
            }
            series = optionalSeries.get();
        }
        long[] bookIdArr = new long[req.ea()];
        for (int i = 0; i < req.ea(); i++) {
            Book book = new Book.BookBuilder(req)
                    .setVer(i + 1)
                    .setSeries(series)
                    .build();
            bookRepository.save(book);
            bookIdArr[i] = book.getId();
        }
        return new Result<>(HttpStatus.OK, bookIdArr, true);
    }

    @Transactional
    public Result<?> addExistingBooks(AddExistingBookRequest req) {
        Optional<Book> optionalBook = bookRepository.findFirstByIsbnOrderByVerDesc(req.isbn());
        if (optionalBook.isEmpty()) {
            return new Result<>("올바르지않은 ISBN 값입니다.", HttpStatus.BAD_REQUEST, false);
        }
        Book existingBook = optionalBook.get();
        int ver = existingBook.getVer();
        long[] bookIdArr = new long[req.ea()];
        for (int i = ver + 1; i < ver + req.ea() + 1; i++) {
            Book book = new Book.BookBuilder(existingBook)
                    .setVer(i)
                    .build();
            bookRepository.save(book);
            bookIdArr[i - ver - 1] = book.getId();
        }
        return new Result<>(HttpStatus.OK, bookIdArr, true);
    }

    @Transactional
    public Result<?> newSeries(AddNewSeriesRequest req) {
        Optional<Series> optionalSeries = seriesRepository.findBySeriesName(req.seriesName());
        if (optionalSeries.isPresent()) {
            return new Result<>("이미 존재하는 시리즈명입니다.", HttpStatus.BAD_REQUEST, false);
        }
        Series series = new Series(req.seriesName());
        seriesRepository.save(series);
        return new Result<>(HttpStatus.OK, series, true);
    }

    @Transactional
    public Result<?> updateBookSeries(UpdateBookSeriesRequest req) {
        Optional<Series> optionalSeries = seriesRepository.findById(req.seriesId());
        if (optionalSeries.isEmpty()) {
            return new Result<>("올바르지 않은 시리즈ID 입니다.", HttpStatus.BAD_REQUEST, false);
        }
        Series series = optionalSeries.get();
        if (!bookRepository.existsByIsbn(req.isbn())) {
            return new Result<>("올바르지않은 ISBN 값입니다.", HttpStatus.BAD_REQUEST, false);
        }
        List<Book> books = bookRepository.findAllByIsbn(req.isbn());
        for (Book book : books) {
            book.updateSeries(series);
            bookRepository.save(book);
        }
        return new Result<>(HttpStatus.OK, books, true);
    }

    public Result<?> searchBooks(BookSearchCondition condition,String keyword,Boolean onlySeries,Boolean onlyDiscard){
        //todo: 추후 쿼리 dsl 로 바꿔서 조건식 달기, 추가해야할 메소드가 너무 많으므로 쿼리 dsl 마이그레이션까지 boolean 관련 검색 로직 제거
        List<LibrarianBookData> data = new ArrayList<>();
        if (Objects.isNull(condition)) {
            for (Book book : bookRepository.findAll()) {
                data.add(new LibrarianBookData(book));
            }
        } else {
            if (condition == BookSearchCondition.AUTHOR){
                for (Book book : bookRepository.findAllByAuthorContaining(keyword)) {
                    data.add(new LibrarianBookData(book));
                }
            }
            if (condition == BookSearchCondition.ISBN){
                for (Book book : bookRepository.findAllByIsbnContaining(keyword)) {
                    data.add(new LibrarianBookData(book));
                }
            }
            if (condition == BookSearchCondition.TITLE){
                for (Book book : bookRepository.findAllByTitleContaining(keyword)) {
                    data.add(new LibrarianBookData(book));
                }
            }
            if (condition == BookSearchCondition.PUBLISHER){
                for (Book book : bookRepository.findAllByPublisherContaining(keyword)) {
                    data.add(new LibrarianBookData(book));
                }
            }
            if (condition == BookSearchCondition.ISNI){
                for (Book book : bookRepository.findAllByIsniContaining(keyword)) {
                    data.add(new LibrarianBookData(book));
                }
            }
            if (condition == BookSearchCondition.SERIES_NAME){
                for (Book book : bookRepository.findAllBySeries_SeriesNameContaining(keyword)) {
                    data.add(new LibrarianBookData(book));
                }
            }
        }
        return new Result<>(HttpStatus.OK,data,true);
    }
}
