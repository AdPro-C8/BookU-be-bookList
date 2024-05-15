package com.adproc8.booku.booklist.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.adproc8.booku.booklist.dto.BookRequestDto;
import com.adproc8.booku.booklist.dto.BookResponseDto;
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

    @GetMapping("")
    List<Book> getBooks(
        @RequestParam Optional<String> author, @RequestParam Optional<String> title,
        @RequestParam Optional<String> sortBy, @RequestParam Optional<String> orderBy)
    {
        Specification<Book> bookSpec = Specification.where(null);

        if (author.isPresent()) {   
            bookSpec = bookSpec.and(authorIs(author.get()));
        }
        if (title.isPresent()) {
            bookSpec = bookSpec.and(titleIs(title.get()));
        }

        List<Book> bookList;

        if (sortBy.isPresent() && orderBy.isPresent()) {
            Sort sort = Sort.by(Direction.fromString(orderBy.get()), sortBy.get());
            bookList = bookService.findAll(bookSpec, sort);
        } else {
            bookList = bookService.findAll(bookSpec);
        }

        return bookList;
    }

    @GetMapping("/{bookId}")
    ResponseEntity<Book> getBook(@PathVariable UUID bookId) {
        Optional<Book> book = bookService.findById(bookId);

        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(book.get());
    }

    @PostMapping("")
    ResponseEntity<BookResponseDto> postBook(@RequestBody BookRequestDto bookDto) {
        Book newBook = Book.builder()
            .title(bookDto.getTitle())
            .author(bookDto.getAuthor())
            .publisher(bookDto.getPublisher())
            .price(bookDto.getPrice())
            .publishDate(bookDto.getPublishDate())
            .isbn(bookDto.getIsbn())
            .pageCount(bookDto.getPageCount())
            .photoUrl(bookDto.getPhotoUrl())
            .category(bookDto.getCategory())
            .build();

        try {
            newBook = bookService.save(newBook);
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).build();
        }

        UUID bookId = newBook.getId();

        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new BookResponseDto(bookId));
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBook(@PathVariable UUID bookId) {
        bookService.deleteById(bookId);
    }
}
