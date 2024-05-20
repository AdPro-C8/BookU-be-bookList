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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.adproc8.booku.booklist.dto.PatchBookRequestDto;
import com.adproc8.booku.booklist.dto.PostBookRequestDto;
import com.adproc8.booku.booklist.dto.PostBookResponseDto;
import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.service.BookService;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private PatchBookRequestDto patchBookDto;

    @Mock
    private PostBookRequestDto bookDto;

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
    void testGetBook() {
        UUID bookId = UUID.randomUUID();
        Book book = Book.builder().id(bookId).build();

        when(bookService.findById(bookId)).thenReturn(Optional.of(book));

        ResponseEntity<Book> responseEntity = bookController.getBook(bookId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(book, responseEntity.getBody());
    }

    @Test
    void testGetBook_NonExistingBook() {
        UUID bookId = UUID.randomUUID();

        when(bookService.findById(bookId)).thenReturn(Optional.empty());

        ResponseEntity<Book> responseEntity = bookController.getBook(bookId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testPostBook_Success() {
        Book newBook = Book.builder()
            .id(UUID.randomUUID())
            .title("Title")
            .author("Author")
            .publisher("Publisher")
            .price(100)
            .publishDate(Date.valueOf("2022-01-01"))
            .isbn("1234567890")
            .pageCount(200)
            .photoUrl("http://example.com/photo.jpg")
            .category("Category")
            .build();

        when(bookDto.getTitle()).thenReturn("Title");
        when(bookDto.getAuthor()).thenReturn("Author");
        when(bookDto.getPublisher()).thenReturn("Publisher");
        when(bookDto.getPrice()).thenReturn(100);
        when(bookDto.getPublishDate()).thenReturn(Date.valueOf("2022-01-01"));
        when(bookDto.getIsbn()).thenReturn("1234567890");
        when(bookDto.getPageCount()).thenReturn(200);
        when(bookDto.getPhotoUrl()).thenReturn("http://example.com/photo.jpg");
        when(bookDto.getCategory()).thenReturn("Category");

        when(bookService.save(any(Book.class))).thenReturn(newBook);

        ResponseEntity<PostBookResponseDto> responseEntity = bookController.postBook(bookDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(newBook.getId(), responseEntity.getBody().getBookId());
    }

    @Test
    void testPostBook_DataIntegrityViolation() {
        when(bookDto.getTitle()).thenReturn("Title");
        when(bookDto.getAuthor()).thenReturn("Author");
        when(bookDto.getPublisher()).thenReturn("Publisher");
        when(bookDto.getPrice()).thenReturn(100);
        when(bookDto.getPublishDate()).thenReturn(Date.valueOf("2022-01-01"));
        when(bookDto.getIsbn()).thenReturn("1234567890");
        when(bookDto.getPageCount()).thenReturn(200);
        when(bookDto.getPhotoUrl()).thenReturn("http://example.com/photo.jpg");
        when(bookDto.getCategory()).thenReturn("Category");

        when(bookService.save(any(Book.class))).thenThrow(DataIntegrityViolationException.class);

        ResponseEntity<PostBookResponseDto> responseEntity = bookController.postBook(bookDto);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void testPostBook_ThrowsException() {
        when(bookDto.getTitle()).thenReturn("Title");
        when(bookDto.getAuthor()).thenReturn("Author");
        when(bookDto.getPublisher()).thenReturn("Publisher");
        when(bookDto.getPrice()).thenReturn(100);
        when(bookDto.getPublishDate()).thenReturn(Date.valueOf("2022-01-01"));
        when(bookDto.getIsbn()).thenReturn("1234567890");
        when(bookDto.getPageCount()).thenReturn(200);
        when(bookDto.getPhotoUrl()).thenReturn("http://example.com/photo.jpg");
        when(bookDto.getCategory()).thenReturn("Category");

        when(bookService.save(any(Book.class))).thenThrow(RuntimeException.class);

        ResponseEntity<PostBookResponseDto> responseEntity = bookController.postBook(bookDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void patchBook_Success() {
        UUID bookId = UUID.randomUUID();
        Book book = Book.builder().id(bookId).build();

        when(bookService.findById(bookId)).thenReturn(Optional.of(book));

        when(patchBookDto.getPublisher()).thenReturn("New Publisher");
        when(patchBookDto.getPublishDate()).thenReturn(Date.valueOf("2022-01-01"));
        when(patchBookDto.getIsbn()).thenReturn("1234567890");
        when(patchBookDto.getPageCount()).thenReturn(200);
        when(patchBookDto.getPhotoUrl()).thenReturn("http://example.com/photo.jpg");
        when(patchBookDto.getCategory()).thenReturn("New Category");

        ResponseEntity<Void> responseEntity = bookController.patchBook(bookId, patchBookDto);

        verify(bookService, times(1)).save(any(Book.class));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void patchBook_NotFound() {
        UUID bookId = UUID.randomUUID();

        when(bookService.findById(bookId)).thenReturn(Optional.empty());

        ResponseEntity<Void> responseEntity = bookController.patchBook(bookId, patchBookDto);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteBook() {
        UUID bookId = UUID.randomUUID();

        bookController.deleteBook(bookId);

        verify(bookService, times(1)).deleteById(bookId);
    }
}
