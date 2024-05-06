package com.adproc8.booku.booklist.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.service.BookService;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

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
}
