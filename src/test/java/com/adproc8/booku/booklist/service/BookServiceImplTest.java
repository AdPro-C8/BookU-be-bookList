package com.adproc8.booku.booklist.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.repository.BookRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void testSave() {
        Book bookToSave = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title")
            .author("Test Author")
            .build();

        when(bookRepository.save(any(Book.class))).thenReturn(bookToSave);

        Book savedBook = bookService.save(bookToSave);

        assertEquals(bookToSave, savedBook);
        verify(bookRepository, times(1)).save(bookToSave);
    }

    @Test
    void testSave_ThrowsException() {
        Book bookToSave = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title")
            .author("Test Author")
            .build();

        doThrow(new RuntimeException()).when(bookRepository).save(any(Book.class));

        assertThrows(RuntimeException.class, () -> bookService.save(bookToSave));
        verify(bookRepository, times(1)).save(bookToSave);
    }

    @Test
    void testSaveAll() {
        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 1")
            .author("Test Author 1")
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 2")
            .author("Test Author 2")
            .build();

        List<Book> booksToSave = Arrays.asList(book1, book2);
        when(bookRepository.saveAll(anyList())).thenReturn(booksToSave);

        List<Book> savedBooks = bookService.saveAll(booksToSave);

        assertEquals(booksToSave, savedBooks);
        verify(bookRepository, times(1)).saveAll(booksToSave);
    }

    @Test
    void testSaveAll_ThrowsException() {
        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 1")
            .author("Test Author 1")
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 2")
            .author("Test Author 2")
            .build();

        List<Book> booksToSave = Arrays.asList(book1, book2);
        doThrow(new RuntimeException()).when(bookRepository).saveAll(anyList());

        assertThrows(RuntimeException.class, () -> bookService.saveAll(booksToSave));
        verify(bookRepository, times(1)).saveAll(booksToSave);
    }

    @Test
    void testFindById_Exists() {
        UUID bookId = UUID.randomUUID();
        Book book = Book.builder()
            .id(bookId)
            .title("Test Title")
            .author("Test Author")
            .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.findById(bookId);

        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testFindById_NotExists() {
        UUID bookId = UUID.randomUUID();

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Optional<Book> foundBook = bookService.findById(bookId);

        assertTrue(foundBook.isEmpty());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testFindById_ThrowsException() {
        UUID bookId = UUID.randomUUID();

        when(bookRepository.findById(bookId)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> bookService.findById(bookId));
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testFindAll() {
        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 1")
            .author("Test Author 1")
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 2")
            .author("Test Author 2")
            .build();

        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> returnedBooks = bookService.findAll();

        assertEquals(books, returnedBooks);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_Empty() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());

        List<Book> returnedBooks = bookService.findAll();

        assertTrue(returnedBooks.isEmpty());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ThrowsException() {
        when(bookRepository.findAll()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> bookService.findAll());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testFindAllWithSort() {
        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 1")
            .author("Test Author 1")
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 2")
            .author("Test Author 2")
            .build();

        List<Book> books = Arrays.asList(book1, book2);
        Sort sort = Sort.by(Sort.Direction.ASC, "title");

        when(bookRepository.findAll(sort)).thenReturn(books);

        List<Book> foundBooks = bookService.findAll(sort);

        assertEquals(books, foundBooks);
        verify(bookRepository, times(1)).findAll(sort);
    }

    @Test
    void testFindAllWithSort_ThrowsException() {
        Sort sort = Sort.by(Sort.Direction.ASC, "title");

        doThrow(new RuntimeException()).when(bookRepository).findAll(sort);

        assertThrows(RuntimeException.class, () -> bookService.findAll(sort));
        verify(bookRepository, times(1)).findAll(sort);
    }

    @Test
    void testFindAllWithSpec() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        Specification<Book> spec = (root, query, cb) -> cb.equal(root.get("title"), "Test Title");

        doReturn(books).when(bookRepository).findAll(spec);

        List<Book> returnedBooks = bookService.findAll(spec);

        assertEquals(books, returnedBooks);
        verify(bookRepository, times(1)).findAll(spec);
    }

    @Test
    void testFindAllWithSpecAndSort() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        Specification<Book> spec = (root, query, cb) -> cb.equal(root.get("title"), "Test Title");

        doReturn(books).when(bookRepository).findAll(spec, sort);

        List<Book> returnedBooks = bookService.findAll(spec, sort);

        assertEquals(books, returnedBooks);
        verify(bookRepository, times(1)).findAll(spec, sort);
    }

    @Test
    void testDeleteById() {
        UUID bookId = UUID.randomUUID();

        doNothing().when(bookRepository).deleteById(bookId);

        bookService.deleteById(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteById_ThrowsException() {
        UUID bookId = UUID.randomUUID();

        doThrow(new EmptyResultDataAccessException(1)).when(bookRepository).deleteById(bookId);

        assertThrows(EmptyResultDataAccessException.class, () -> bookService.deleteById(bookId));

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteAll() {
        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 1")
            .author("Test Author 1")
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 2")
            .author("Test Author 2")
            .build();

        List<Book> booksToDelete = Arrays.asList(book1, book2);

        doNothing().when(bookRepository).deleteAllInBatch(anyList());

        bookService.deleteAll(booksToDelete);

        verify(bookRepository, times(1)).deleteAllInBatch(booksToDelete);
    }

    @Test
    void testDeleteAll_ThrowsException() {
        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 1")
            .author("Test Author 1")
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 2")
            .author("Test Author 2")
            .build();

        List<Book> booksToDelete = Arrays.asList(book1, book2);

        doThrow(new RuntimeException()).when(bookRepository).deleteAllInBatch(anyList());

        assertThrows(RuntimeException.class, () -> bookService.deleteAll(booksToDelete));
        verify(bookRepository, times(1)).deleteAllInBatch(booksToDelete);
    }
}
