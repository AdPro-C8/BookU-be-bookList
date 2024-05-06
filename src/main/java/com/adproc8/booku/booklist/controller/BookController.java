package com.adproc8.booku.booklist.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.service.BookService;

import static com.adproc8.booku.booklist.repository.BookRepository.BookSpecifications.*;

@RestController
@RequestMapping("/book")
class BookController {

    private final BookService bookService;

    @Autowired
    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    List<Book> getBooks(
        @RequestParam Optional<String> author, @RequestParam Optional<String> title,
        @RequestParam Optional<String> sortBy, @RequestParam Optional<String> sortOrder)
    {
        Specification<Book> bookSpec = Specification.where(null);

        if (author.isPresent()) {   
            bookSpec = bookSpec.and(authorIs(author.get()));
        }
        if (title.isPresent()) {
            bookSpec = bookSpec.and(titleIs(title.get()));
        }

        List<Book> bookList;

        if (sortBy.isPresent() && sortOrder.isPresent()) {
            Sort sort = Sort.by(Direction.fromString(sortOrder.get()), sortBy.get());
            bookList = bookService.findAll(bookSpec, sort);
        } else {
            bookList = bookService.findAll(bookSpec);
        }

        return bookList;
    }
}
