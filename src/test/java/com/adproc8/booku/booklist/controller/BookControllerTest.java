package com.adproc8.booku.booklist.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.Iterator;
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

import com.adproc8.booku.booklist.dto.*;
import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.service.BookService;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private GetBooksByIdRequestDto getBooksByIdDto;

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
    void testGetAllBooks_NoParams() {
        when(bookService.findAll(any(Specification.class))).thenReturn(dummyBooks);

        List<Book> books = bookController.getAllBooks(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(dummyBooks, books);
    }

    @Test
    void testGetAllBooks_AuthorParam() {
        when(bookService.findAll(any(Specification.class))).thenReturn(dummyBooks);

        List<Book> books = bookController.getAllBooks(Optional.of("Author 1"), Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(dummyBooks, books);
    }

    @Test
    void testGetAllBooks_TitleParam() {
        when(bookService.findAll(any(Specification.class))).thenReturn(dummyBooks);

        List<Book> books = bookController.getAllBooks(Optional.empty(), Optional.of("Title 1"), Optional.empty(), Optional.empty());

        assertEquals(dummyBooks, books);
    }

    @Test
    void testGetAllBooks_SortParams() {
        when(bookService.findAll(any(Specification.class), any(Sort.class))).thenReturn(dummyBooks);

        List<Book> books = bookController.getAllBooks(Optional.empty(), Optional.empty(), Optional.of("title"), Optional.of("asc"));

        assertEquals(dummyBooks, books);
    }

    @Test
    void testGetAllBooks_AllParams() {
        when(bookService.findAll(any(Specification.class), any(Sort.class))).thenReturn(dummyBooks);

        List<Book> books = bookController.getAllBooks(Optional.of("Author 1"), Optional.of("Title 1"), Optional.of("title"), Optional.of("asc"));

        assertEquals(dummyBooks, books);
    }

    @Test
    void testGetBookById() {
        UUID bookId = UUID.randomUUID();
        Book book = Book.builder().id(bookId).build();

        when(bookService.findById(bookId)).thenReturn(Optional.of(book));

        ResponseEntity<Book> responseEntity = bookController.getBookById(bookId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(book, responseEntity.getBody());
    }

    @Test
    void testGetBookById_NonExistingBook() {
        UUID bookId = UUID.randomUUID();

        when(bookService.findById(bookId)).thenReturn(Optional.empty());

        ResponseEntity<Book> responseEntity = bookController.getBookById(bookId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testCreateBook_Success() {
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

        ResponseEntity<PostBookResponseDto> responseEntity = bookController.createBook(bookDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(newBook.getId(), responseEntity.getBody().getBookId());
    }

    @Test
    void testCreateBook_DataIntegrityViolation() {
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

        ResponseEntity<PostBookResponseDto> responseEntity = bookController.createBook(bookDto);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void testCreateBook_ThrowsException() {
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

        ResponseEntity<PostBookResponseDto> responseEntity = bookController.createBook(bookDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void testGetMultipleBooksById() {
        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();

        Book book1 = Book.builder()
                .id(bookId1)
                .build();
        Book book2 = Book.builder()
                .id(bookId2)
                .build();

        List<UUID> bookIds = List.of(bookId1, bookId2);
        List<Book> books = List.of(book1, book2);
        when(bookService.findAllById(bookIds)).thenReturn(books);

        when(getBooksByIdDto.getBookIds()).thenReturn(bookIds);

        ResponseEntity<List<Book>> responseEntity =
                bookController.getMultipleBooksById(getBooksByIdDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Book> returnedBooks = responseEntity.getBody();

        assertNotNull(returnedBooks);
        assertEquals(2, returnedBooks.size());

        Iterator<Book> returnedBooksIterator = returnedBooks.iterator();

        assertEquals(book1, returnedBooksIterator.next());
        assertEquals(book2, returnedBooksIterator.next());
        assertFalse(returnedBooksIterator.hasNext());
    }

    @Test
    void testGetMultipleBooksById_InvalidDto() {
        when(getBooksByIdDto.getBookIds()).thenThrow(NullPointerException.class);

        ResponseEntity<List<Book>> responseEntity = bookController
                .getMultipleBooksById(getBooksByIdDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateBookById_Success() {
        UUID bookId = UUID.randomUUID();
        Book book = Book.builder().id(bookId).build();

        when(bookService.findById(bookId)).thenReturn(Optional.of(book));

        when(patchBookDto.getPublisher()).thenReturn("New Publisher");
        when(patchBookDto.getPublishDate()).thenReturn(Date.valueOf("2022-01-01"));
        when(patchBookDto.getIsbn()).thenReturn("1234567890");
        when(patchBookDto.getPageCount()).thenReturn(200);
        when(patchBookDto.getPhotoUrl()).thenReturn("http://example.com/photo.jpg");
        when(patchBookDto.getCategory()).thenReturn("New Category");

        ResponseEntity<Void> responseEntity = bookController.updateBookById(bookId, patchBookDto);

        verify(bookService, times(1)).save(any(Book.class));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateBookById_NotFound() {
        UUID bookId = UUID.randomUUID();

        when(bookService.findById(bookId)).thenReturn(Optional.empty());

        ResponseEntity<Void> responseEntity = bookController.updateBookById(bookId, patchBookDto);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteBookById() {
        UUID bookId = UUID.randomUUID();

        bookController.deleteBookById(bookId);

        verify(bookService, times(1)).deleteById(bookId);
    }
}
