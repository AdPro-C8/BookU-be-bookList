package com.adproc8.booku.booklist.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;

import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.repository.BookRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
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
    void testFindByAuthor() {
        String author = "Test Author";
        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 1")
            .author(author)
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 2")
            .author(author)
            .build();

        List<Book> expectedBooks = Arrays.asList(book1, book2);
        when(bookRepository.findByAuthor(author)).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.findByAuthor(author);

        assertEquals(expectedBooks, actualBooks);
        verify(bookRepository, times(1)).findByAuthor(author);
    }

    @Test
    void testFindByAuthor_NoBooks() {
        String author = "Nonexistent Author";
        when(bookRepository.findByAuthor(author)).thenReturn(new ArrayList<>());

        List<Book> actualBooks = bookService.findByAuthor(author);

        assertTrue(actualBooks.isEmpty());
        verify(bookRepository, times(1)).findByAuthor(author);
    }

    @Test
    void testFindByAuthor_NullAuthor() {
        when(bookRepository.findByAuthor(null)).thenReturn(new ArrayList<>());

        List<Book> actualBooks = bookService.findByAuthor(null);

        assertTrue(actualBooks.isEmpty());
        verify(bookRepository, times(1)).findByAuthor(null);
    }

    @Test
    void testFindByAuthorWithSort() {
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        String author = "Test Author";

        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 1")
            .author(author)
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title 2")
            .author(author)
            .build();

        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findByAuthor(anyString(), any(Sort.class))).thenReturn(books);

        List<Book> foundBooks = bookService.findByAuthor(author, sort);

        assertEquals(books, foundBooks);
        verify(bookRepository, times(1)).findByAuthor(author, sort);
    }

    @Test
    void testFindByAuthorWithSort_ThrowsException() {
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        String author = "Test Author";

        doThrow(new RuntimeException()).when(bookRepository).findByAuthor(anyString(), any(Sort.class));

        assertThrows(RuntimeException.class, () -> bookService.findByAuthor(author, sort));
        verify(bookRepository, times(1)).findByAuthor(author, sort);
    }

    @Test
    void testFindByTitle() {
        String title = "Test Title";
        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title(title)
            .author("Test Author 1")
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title(title)
            .author("Test Author 2")
            .build();

        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepository.findByTitle(anyString())).thenReturn(books);

        List<Book> foundBooks = bookService.findByTitle(title);

        assertEquals(books, foundBooks);
        verify(bookRepository, times(1)).findByTitle(title);
    }

    @Test
    void testFindByTitle_NoBooksFound() {
        String title = "Nonexistent Title";
        when(bookRepository.findByTitle(anyString())).thenReturn(new ArrayList<>());

        List<Book> foundBooks = bookService.findByTitle(title);

        assertTrue(foundBooks.isEmpty());
        verify(bookRepository, times(1)).findByTitle(title);
    }

    @Test
    void testFindByTitleWithSort() {
        String title = "Test Title";
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title(title)
            .author("Test Author 1")
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title(title)
            .author("Test Author 2")
            .build();

        List<Book> expectedBooks = Arrays.asList(book1, book2);
        when(bookRepository.findByTitle(anyString(), any(Sort.class))).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.findByTitle(title, sort);

        assertEquals(expectedBooks, actualBooks);
        verify(bookRepository, times(1)).findByTitle(title, sort);
    }

    @Test
    void testFindByTitleWithSort_NoBooksFound() {
        String title = "Nonexistent Title";
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        when(bookRepository.findByTitle(anyString(), any(Sort.class))).thenReturn(new ArrayList<>());

        List<Book> actualBooks = bookService.findByTitle(title, sort);

        assertTrue(actualBooks.isEmpty());
        verify(bookRepository, times(1)).findByTitle(title, sort);
    }

    @Test
    void testFindByTitleWithSort_ThrowsException() {
        String title = "Test Title";
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        doThrow(new RuntimeException()).when(bookRepository).findByTitle(anyString(), any(Sort.class));

        assertThrows(RuntimeException.class, () -> bookService.findByTitle(title, sort));
        verify(bookRepository, times(1)).findByTitle(title, sort);
    }

    @Test
    void testFindByTitleAndAuthor() {
        String title = "Test Title";
        String author = "Test Author";

        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title(title)
            .author(author)
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title(title)
            .author(author)
            .build();

        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findByTitleAndAuthor(title, author)).thenReturn(books);

        List<Book> foundBooks = bookService.findByTitleAndAuthor(title, author);

        assertEquals(books, foundBooks);
        verify(bookRepository, times(1)).findByTitleAndAuthor(title, author);
    }

    @Test
    void testFindByTitleAndAuthor_NoBooksFound() {
        String title = "Nonexistent Title";
        String author = "Nonexistent Author";

        when(bookRepository.findByTitleAndAuthor(title, author)).thenReturn(new ArrayList<>());

        List<Book> foundBooks = bookService.findByTitleAndAuthor(title, author);

        assertTrue(foundBooks.isEmpty());
        verify(bookRepository, times(1)).findByTitleAndAuthor(title, author);
    }

    @Test
    void testFindByTitleAndAuthorWithSort() {
        String title = "Test Title";
        String author = "Test Author";
        Sort sort = Sort.by(Sort.Direction.ASC, "title");

        Book book1 = Book.builder()
            .id(UUID.randomUUID())
            .title(title)
            .author(author)
            .build();

        Book book2 = Book.builder()
            .id(UUID.randomUUID())
            .title(title)
            .author(author)
            .build();

        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findByTitleAndAuthor(title, author, sort)).thenReturn(books);

        List<Book> foundBooks = bookService.findByTitleAndAuthor(title, author, sort);

        assertEquals(books, foundBooks);
        verify(bookRepository, times(1)).findByTitleAndAuthor(title, author, sort);
    }

    @Test
    void testFindByTitleAndAuthorWithSort_EmptyResult() {
        String title = "Nonexistent Title";
        String author = "Nonexistent Author";
        Sort sort = Sort.by(Sort.Direction.ASC, "title");

        when(bookRepository.findByTitleAndAuthor(title, author, sort)).thenReturn(new ArrayList<>());

        List<Book> foundBooks = bookService.findByTitleAndAuthor(title, author, sort);

        assertTrue(foundBooks.isEmpty());
        verify(bookRepository, times(1)).findByTitleAndAuthor(title, author, sort);
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
