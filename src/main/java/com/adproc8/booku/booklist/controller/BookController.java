package com.adproc8.booku.booklist.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.adproc8.booku.booklist.dto.GetBooksByIdRequestDto;
import com.adproc8.booku.booklist.dto.PatchBookRequestDto;
import com.adproc8.booku.booklist.dto.PostBookRequestDto;
import com.adproc8.booku.booklist.dto.PostBookResponseDto;
import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.service.BookService;

import static com.adproc8.booku.booklist.repository.BookRepository.BookSpecifications.*;

@RestController
@RequestMapping("/book")
class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    @Autowired
    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<Book> getAllBooks(
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
    ResponseEntity<Book> getBookById(@PathVariable UUID bookId) {
        Optional<Book> book = bookService.findById(bookId);

        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(book.get());
    }

    @PostMapping("/get-multiple")
    ResponseEntity<List<Book>> getMultipleBooksById(@RequestBody GetBooksByIdRequestDto dto) {
        List<Book> books;

        try {
            books = bookService.findAllById(dto.getBookIds());
        } catch (RuntimeException exception) {
            logger.error(exception.getMessage(), exception);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(books);
    }

    @PostMapping("")
    ResponseEntity<PostBookResponseDto> createBook(@RequestBody PostBookRequestDto bookDto) {
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
            logger.error(exception.getMessage(), exception);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).build();
        } catch (RuntimeException exception) {
            logger.error(exception.getMessage(), exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
        }

        UUID bookId = newBook.getId();

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(new PostBookResponseDto(bookId));
    }

    @PatchMapping("/{bookId}")
    ResponseEntity<Void> updateBookById(
        @PathVariable UUID bookId,
        @RequestBody PatchBookRequestDto bookDto)
    {
        Optional<Book> book = bookService.findById(bookId);

        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Book someBook = book.get();

        Optional.ofNullable(bookDto.getPublisher())
                .ifPresent(publisher -> someBook.setPublisher(publisher));
        Optional.ofNullable(bookDto.getPublishDate())
                .ifPresent(publishDate -> someBook.setPublishDate(publishDate));
        Optional.ofNullable(bookDto.getIsbn())
                .ifPresent(isbn -> someBook.setIsbn(isbn));
        Optional.ofNullable(bookDto.getPageCount())
                .ifPresent(pageCount -> someBook.setPageCount(pageCount));
        Optional.ofNullable(bookDto.getPhotoUrl())
                .ifPresent(photoUrl -> someBook.setPhotoUrl(photoUrl));
        Optional.ofNullable(bookDto.getCategory())
                .ifPresent(category -> someBook.setCategory(category));

        bookService.save(someBook);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    void deleteBookById(@PathVariable UUID bookId) {
        bookService.deleteById(bookId);
    }
}
