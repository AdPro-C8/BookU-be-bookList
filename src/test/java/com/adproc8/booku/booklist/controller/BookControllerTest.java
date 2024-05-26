package com.adproc8.booku.booklist.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.adproc8.booku.booklist.dto.*;
import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private GetBooksByIdRequestDto getBooksByIdDto;

    @MockBean
    private PatchBookRequestDto patchBookDto;

    @MockBean
    private PostBookRequestDto bookDto;

    @MockBean
    private BookService bookService;

    @Autowired
    private BookController bookController;

    @Autowired
    private MockMvc mockMvc;

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
    void testGetBookById() throws Exception {
        UUID bookId = UUID.randomUUID();
        Book book = Book.builder().id(bookId).build();

        when(bookService.findById(bookId)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/book/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBooksByIdDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId().toString()));
    }

    @Test
    void testGetBookById_NonExistingBook() throws Exception {
        UUID bookId = UUID.randomUUID();

        when(bookService.findById(bookId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/book/" + bookId)).andExpect(status().isNotFound());
    }

    @Test
    void testCreateBook_Success() throws Exception {
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

        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(newBook.getId().toString()));
    }

    @Test
    void testCreateBook_DataIntegrityViolation() throws Exception {
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

        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateBook_ThrowsException() throws Exception {
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

        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetMultipleBooksById() throws Exception {
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

        mockMvc.perform(post("/book/get-multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBooksByIdDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookId1.toString()))
                .andExpect(jsonPath("$[1].id").value(bookId2.toString()));
    }

    @Test
    void testGetMultipleBooksById_InvalidDto() throws Exception {
        when(getBooksByIdDto.getBookIds()).thenThrow(NullPointerException.class);

        mockMvc.perform(post("/book/get-multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBooksByIdDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBookById_Success() throws Exception {
        UUID bookId = UUID.randomUUID();
        Book book = Book.builder().id(bookId).build();

        when(bookService.findById(bookId)).thenReturn(Optional.of(book));

        when(patchBookDto.getPublisher()).thenReturn("New Publisher");
        when(patchBookDto.getPublishDate()).thenReturn(Date.valueOf("2022-01-01"));
        when(patchBookDto.getIsbn()).thenReturn("1234567890");
        when(patchBookDto.getPageCount()).thenReturn(200);
        when(patchBookDto.getPhotoUrl()).thenReturn("http://example.com/photo.jpg");
        when(patchBookDto.getCategory()).thenReturn("New Category");

        mockMvc.perform(patch("/book/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBooksByIdDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateBookById_NotFound() throws Exception {
        UUID bookId = UUID.randomUUID();

        when(bookService.findById(bookId)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/book/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBooksByIdDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBookById() {
        UUID bookId = UUID.randomUUID();

        bookController.deleteBookById(bookId);

        verify(bookService, times(1)).deleteById(bookId);
    }
}
