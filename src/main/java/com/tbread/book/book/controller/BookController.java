package com.tbread.book.book.controller;

import com.tbread.book.book.dto.request.AddBookRequest;
import com.tbread.book.book.dto.request.AddExistingBookRequest;
import com.tbread.book.book.dto.request.AddNewSeriesRequest;
import com.tbread.book.book.dto.request.UpdateBookSeriesRequest;
import com.tbread.book.book.entity.enums.BookSearchCondition;
import com.tbread.book.book.service.BookService;
import com.tbread.book.common.dto.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("new")
    public ResponseEntity addNewBooks(@Valid @RequestBody AddBookRequest req, BindingResult br){
        if (br.hasErrors()) {
            return new Result<>(br.getAllErrors().getFirst().getDefaultMessage(),HttpStatus.BAD_REQUEST,false).publish();
        }
        return bookService.addNewBooks(req).publish();
    }

    @PostMapping("add")
    public ResponseEntity addBook(@Valid @RequestBody AddExistingBookRequest req, BindingResult br){
        if (br.hasErrors()) {
            return new Result<>(br.getAllErrors().getFirst().getDefaultMessage(),HttpStatus.BAD_REQUEST,false).publish();
        }
        return bookService.addExistingBooks(req).publish();
    }

    @PostMapping("series/new")
    public ResponseEntity newSeries(@Valid @RequestBody AddNewSeriesRequest req,BindingResult br){
        if (br.hasErrors()) {
            return new Result<>(br.getAllErrors().getFirst().getDefaultMessage(),HttpStatus.BAD_REQUEST,false).publish();
        }
        return bookService.newSeries(req).publish();
    }

    @PostMapping("series/add")
    public ResponseEntity addSeries(@Valid @RequestBody UpdateBookSeriesRequest req, BindingResult br){
        if (br.hasErrors()) {
            return new Result<>(br.getAllErrors().getFirst().getDefaultMessage(),HttpStatus.BAD_REQUEST,false).publish();
        }
        return bookService.updateBookSeries(req).publish();
    }

    @GetMapping("search")
    public ResponseEntity searchBook(@RequestParam BookSearchCondition condition,
                                     @RequestParam(defaultValue = "") String keyword,
                                     @RequestParam boolean onlySeries,
                                     @RequestParam boolean onlyDiscard){
        return bookService.searchBooks(condition, keyword, onlySeries, onlyDiscard).publish();
    }

    @GetMapping("series/search")
    public ResponseEntity searchSeries(@RequestParam(defaultValue = "") String keyword){
        return bookService.searchSeries(keyword).publish();
    }
}
