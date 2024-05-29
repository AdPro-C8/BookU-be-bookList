package com.adproc8.booku.booklist.controller;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.adproc8.booku.booklist.dto.GetBooksByIdRequestDto;
import com.adproc8.booku.booklist.dto.PatchBookRequestDto;
import com.adproc8.booku.booklist.dto.PatchBooksByIdRequestDto;
import com.adproc8.booku.booklist.dto.PostBookRequestDto;
import com.adproc8.booku.booklist.dto.PostBookResponseDto;
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

        if (!sortBy.isPresent() || !orderBy.isPresent()) {
            return bookService.findAll(bookSpec);
        }

        Direction direction;
        try {
            direction = Direction.fromString(orderBy.get());
        } catch (IllegalArgumentException exception) {
            return bookService.findAll(bookSpec);
        }

        String property = sortBy.get();
        List<Book> books = bookService.findAll(bookSpec, Sort.by(direction, property));

        return books;
    }

    @GetMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    Book getBookById(@PathVariable UUID bookId) {
        return bookService.findById(bookId).get();
    }

    @PostMapping("/get-multiple")
    @ResponseStatus(HttpStatus.OK)
    List<Book> getMultipleBooksById(@RequestBody GetBooksByIdRequestDto dto) {
        return bookService.findAllById(dto.getBookIds());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    PostBookResponseDto createBook(@RequestBody PostBookRequestDto bookDto) {
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

        newBook = bookService.save(newBook);

        UUID bookId = newBook.getId();

        return new PostBookResponseDto(bookId);
    }

    @PatchMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    void updateBookById(
        @PathVariable UUID bookId,
        @RequestBody PatchBookRequestDto bookDto)
    {
        Book book = bookService.findById(bookId).get();

        Optional.ofNullable(bookDto.getPublisher())
                .ifPresent(publisher -> book.setPublisher(publisher));
        Optional.ofNullable(bookDto.getPrice())
                .ifPresent(price -> book.setPrice(price));
        Optional.ofNullable(bookDto.getPublishDate())
                .ifPresent(publishDate -> book.setPublishDate(publishDate));
        Optional.ofNullable(bookDto.getIsbn())
                .ifPresent(isbn -> book.setIsbn(isbn));
        Optional.ofNullable(bookDto.getPageCount())
                .ifPresent(pageCount -> book.setPageCount(pageCount));
        Optional.ofNullable(bookDto.getPhotoUrl())
                .ifPresent(photoUrl -> book.setPhotoUrl(photoUrl));
        Optional.ofNullable(bookDto.getCategory())
                .ifPresent(category -> book.setCategory(category));
        Optional.ofNullable(bookDto.getDownloadCount())
                .ifPresent(downloadCount -> book.setDownloadCount(downloadCount));

        bookService.save(book);
    }

    @PatchMapping("")
    @ResponseStatus(HttpStatus.OK)
    void updateMultipleBooksById(
        @RequestBody PatchBooksByIdRequestDto patchBooksByIdDto)
    {
        List<PatchBookRequestDto> patchBookDtos = patchBooksByIdDto.getPatchBookDtos();
        List<UUID> bookIds = patchBookDtos.stream()
                .map(PatchBookRequestDto::getId)
                .toList();

        List<Book> books = bookService.findAllById(bookIds);

        ListIterator<Book> booksIterator = books.listIterator();
        ListIterator<PatchBookRequestDto> patchBookDtosIterator =
                patchBookDtos.listIterator();

        while (booksIterator.hasNext() && patchBookDtosIterator.hasNext()) {
            Book book = booksIterator.next();
            PatchBookRequestDto bookDto = patchBookDtosIterator.next();

            Optional.ofNullable(bookDto.getPublisher())
                    .ifPresent(publisher -> book.setPublisher(publisher));
            Optional.ofNullable(bookDto.getPrice())
                    .ifPresent(price -> book.setPrice(price));
            Optional.ofNullable(bookDto.getPublishDate())
                    .ifPresent(publishDate -> book.setPublishDate(publishDate));
            Optional.ofNullable(bookDto.getIsbn())
                    .ifPresent(isbn -> book.setIsbn(isbn));
            Optional.ofNullable(bookDto.getPageCount())
                    .ifPresent(pageCount -> book.setPageCount(pageCount));
            Optional.ofNullable(bookDto.getPhotoUrl())
                    .ifPresent(photoUrl -> book.setPhotoUrl(photoUrl));
            Optional.ofNullable(bookDto.getCategory())
                    .ifPresent(category -> book.setCategory(category));
            Optional.ofNullable(bookDto.getDownloadCount())
                    .ifPresent(downloadCount -> book.setDownloadCount(downloadCount));
        }

        bookService.saveAll(books);
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    void deleteBookById(@PathVariable UUID bookId) {
        bookService.deleteById(bookId);
    }
}
