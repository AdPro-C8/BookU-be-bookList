package com.adproc8.booku.booklist.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort.Direction;

import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.repository.BookRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    private AutoCloseable closeable;

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookServiceImpl bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSave() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        Book savedBook = bookService.save(book);
        assertEquals(book, savedBook);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testSaveAll() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.saveAll(anyList())).thenReturn(books);
        List<Book> savedBooks = bookService.saveAll(books);
        assertEquals(books, savedBooks);
        verify(bookRepository, times(1)).saveAll(books);
    }

    @Test
    void testFindAll() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);
        List<Book> foundBooks = bookService.findAll();
        assertEquals(books, foundBooks);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testFindByAuthor() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findByAuthor(anyString())).thenReturn(books);
        List<Book> foundBooks = bookService.findByAuthor("Test Author");
        assertEquals(books, foundBooks);
        verify(bookRepository, times(1)).findByAuthor("Test Author");
    }

    @Test
    void testFindByTitle() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findByTitle(anyString())).thenReturn(books);
        List<Book> foundBooks = bookService.findByTitle("Test Title");
        assertEquals(books, foundBooks);
        verify(bookRepository, times(1)).findByTitle("Test Title");
    }

    @Test
    void testFindByOrderByPublishDate() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findByOrderByPublishDateAsc()).thenReturn(books);
        List<Book> foundBooks = bookService.findByOrderByPublishDate(Direction.ASC);
        assertEquals(books, foundBooks);
        verify(bookRepository, times(1)).findByOrderByPublishDateAsc();

        when(bookRepository.findByOrderByPublishDateDesc()).thenReturn(books);
        foundBooks = bookService.findByOrderByPublishDate(Direction.DESC);
        assertEquals(books, foundBooks);
        verify(bookRepository, times(1)).findByOrderByPublishDateDesc();
    }

    @Test
    void testDeleteById() {
        doNothing().when(bookRepository).deleteById(any(UUID.class));
        bookService.deleteById(book.getId());
        verify(bookRepository, times(1)).deleteById(book.getId());
    }

    @Test
    void testDeleteAll() {
        List<Book> books = Arrays.asList(book);
        doNothing().when(bookRepository).deleteAllInBatch(anyList());
        bookService.deleteAll(books);
        verify(bookRepository, times(1)).deleteAllInBatch(books);
    }
}
