package com.adproc8.booku.booklist.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.service.BookService;

@RestController
@RequestMapping("/book")
class BookController {

    private BookService bookService;

    @Autowired
    BookController(BookService bookService) {
    }

    @GetMapping
    List<Book> getBooks(
        @RequestParam Optional<String> author, @RequestParam Optional<String> title,
        @RequestParam Optional<String> sortBy, @RequestParam Optional<String> sortOrder)
    {
        return null;
    }
}
