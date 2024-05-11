package com.adproc8.booku.booklist.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.adproc8.booku.booklist.dto.BookRequestDto;
import com.adproc8.booku.booklist.dto.BookResponseDto;
import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.service.BookService;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookRequestDto bookDto;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private List<Book> dummyBooks;

    @BeforeEach
    void setUp() {
        Book book1 = Book.builder().author("Author 1").title("Title 1").build();
        Book book2 = Book.builder().author("Author 2").title("Title 2").build();
        dummyBooks = Arrays.asList(book1, book2);
    }

    @Test
    void testGetBooks_NoParams() {
        when(bookService.findAll(any(Specification.class))).thenReturn(dummyBooks);

        List<Book> books = bookController.getBooks(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(dummyBooks, books);
    }

    @Test
    void testGetBooks_AuthorParam() {
        when(bookService.findAll(any(Specification.class))).thenReturn(dummyBooks);

        List<Book> books = bookController.getBooks(Optional.of("Author 1"), Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(dummyBooks, books);
    }

    @Test
    void testGetBooks_TitleParam() {
        when(bookService.findAll(any(Specification.class))).thenReturn(dummyBooks);

        List<Book> books = bookController.getBooks(Optional.empty(), Optional.of("Title 1"), Optional.empty(), Optional.empty());

        assertEquals(dummyBooks, books);
    }

    @Test
    void testGetBooks_SortParams() {
        when(bookService.findAll(any(Specification.class), any(Sort.class))).thenReturn(dummyBooks);

        List<Book> books = bookController.getBooks(Optional.empty(), Optional.empty(), Optional.of("title"), Optional.of("asc"));

        assertEquals(dummyBooks, books);
    }

    @Test
    void testGetBooks_AllParams() {
        when(bookService.findAll(any(Specification.class), any(Sort.class))).thenReturn(dummyBooks);

        List<Book> books = bookController.getBooks(Optional.of("Author 1"), Optional.of("Title 1"), Optional.of("title"), Optional.of("asc"));

        assertEquals(dummyBooks, books);
    }

    @Test
    void testPostBook() {
        Book newBook = Book.builder()
            .title("Title")
            .author("Author")
            .publisher("Publisher")
            .price(100)
            .publishDate(Date.valueOf("2022-01-01"))
            .isbn("1234567890")
            .pageCount(200)
            .photoUrl("http://example.com/photo.jpg")
            .build();

        when(bookService.save(any(Book.class))).thenReturn(newBook);

        when(bookDto.getTitle()).thenReturn("Title");
        when(bookDto.getAuthor()).thenReturn("Author");
        when(bookDto.getPublisher()).thenReturn("Publisher");
        when(bookDto.getPrice()).thenReturn(100);
        when(bookDto.getPublishDate()).thenReturn(Date.valueOf("2022-01-01"));
        when(bookDto.getIsbn()).thenReturn("1234567890");
        when(bookDto.getPageCount()).thenReturn(200);
        when(bookDto.getPhotoUrl()).thenReturn("http://example.com/photo.jpg");

        BookResponseDto responseDto = bookController.postBook(bookDto);

        assertEquals(newBook.getId(), responseDto.getBookId());
    }

    @Test
    void testDeleteBook() {
        UUID bookId = UUID.randomUUID();

        bookController.deleteBook(bookId);

        verify(bookService, times(1)).deleteById(bookId);
    }
}
