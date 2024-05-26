package com.adproc8.booku.booklist.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.repository.BookRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
        doThrow(IllegalArgumentException.class).when(bookRepository).save(null);

        assertThrows(IllegalArgumentException.class, () -> bookService.save(null));
        verify(bookRepository, times(1)).save(null);
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
        Book book = Book.builder()
            .id(UUID.randomUUID())
            .title("Test Title")
            .author("Test Author")
            .build();

        List<Book> booksToSave = new ArrayList<>();
        booksToSave.add(book);
        booksToSave.add(null);
        doThrow(IllegalArgumentException.class).when(bookRepository).saveAll(anyList());

        assertThrows(IllegalArgumentException.class, () -> bookService.saveAll(booksToSave));
        verify(bookRepository, times(1)).saveAll(booksToSave);
    }

    @Test
    void testFindById() {
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
        when(bookRepository.findById(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> bookService.findById(null));
        verify(bookRepository, times(1)).findById(null);
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
    void testFindAllById() {
        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();

        List<UUID> bookIds = List.of(bookId1, bookId2);

        Book book1 = Book.builder()
            .id(bookId1)
            .title("Test Title 1")
            .author("Test Author 1")
            .build();

        Book book2 = Book.builder()
            .id(bookId2)
            .title("Test Title 2")
            .author("Test Author 2")
            .build();

        List<Book> books = List.of(book1, book2);
        when(bookRepository.findAllById(bookIds)).thenReturn(books);

        List<Book> returnedBooks = bookService.findAllById(bookIds);

        assertNotNull(returnedBooks);

        Iterator<Book> returnedBooksIterator = returnedBooks.iterator();

        assertTrue(returnedBooksIterator.hasNext());
        assertEquals(book1, returnedBooksIterator.next());
        assertEquals(book2, returnedBooksIterator.next());
        assertFalse(returnedBooksIterator.hasNext());
    }

    @Test
    void testFindAllById_ThrowsException() {
        List<UUID> bookIds = new ArrayList<>();
        bookIds.add(UUID.randomUUID());
        bookIds.add(null);

        when(bookRepository.findAllById(bookIds)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> bookService.findAllById(bookIds));
    }

    @Test
    void testFindAllById_NotEmpty_NotNull() {
        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();

        Set<UUID> bookIds = new HashSet<>(List.of(bookId1, bookId2));

        Specification<Book> spec = Specification.where(null);

        Book book1 = Book.builder()
            .id(bookId1)
            .title("Test Title 1")
            .author("Test Author 1")
            .build();

        Book book2 = Book.builder()
            .id(bookId2)
            .title("Test Title 2")
            .author("Test Author 2")
            .build();

        List<Book> books = List.of(book1, book2);
        when(bookRepository.findAll(spec)).thenReturn(books);

        List<Book> returnedBooks = bookService.findAllById(bookIds, spec);

        assertNotNull(returnedBooks);

        Iterator<Book> returnedBooksIterator = returnedBooks.iterator();

        assertTrue(returnedBooksIterator.hasNext());
        assertEquals(book1, returnedBooksIterator.next());
        assertEquals(book2, returnedBooksIterator.next());
        assertFalse(returnedBooksIterator.hasNext());

        verify(bookRepository, times(1)).findAll(spec);
    }

    @Test
    void testFindAllById_Empty_NotNull() {
        Set<UUID> bookIds = new HashSet<>();

        Specification<Book> spec = Specification.where(null);

        when(bookRepository.findAll(spec)).thenReturn(new ArrayList<>());

        List<Book> returnedBooks = bookService.findAllById(bookIds, spec);

        assertNotNull(returnedBooks);
        assertTrue(returnedBooks.isEmpty());

        verify(bookRepository, times(1)).findAll(spec);
    }

    @Test
    void testFindAllById_NotEmpty_Null() {
        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();

        Set<UUID> bookIds = new HashSet<>(List.of(bookId1, bookId2));

        Book book1 = Book.builder()
            .id(bookId1)
            .title("Test Title 1")
            .author("Test Author 1")
            .build();

        Book book2 = Book.builder()
            .id(bookId2)
            .title("Test Title 2")
            .author("Test Author 2")
            .build();

        List<Book> books = List.of(book1, book2);
        when(bookRepository.findAll(any(Specification.class))).thenReturn(books);

        List<Book> returnedBooks = bookService.findAllById(bookIds, null);

        assertNotNull(returnedBooks);

        Iterator<Book> returnedBooksIterator = returnedBooks.iterator();

        assertTrue(returnedBooksIterator.hasNext());
        assertEquals(book1, returnedBooksIterator.next());
        assertEquals(book2, returnedBooksIterator.next());
        assertFalse(returnedBooksIterator.hasNext());

        verify(bookRepository, times(1)).findAll(any(Specification.class));
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
        doThrow(IllegalArgumentException.class).when(bookRepository).deleteById(null);

        assertThrows(IllegalArgumentException.class, () -> bookService.deleteById(null));
        verify(bookRepository, times(1)).deleteById(null);
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
        doThrow(IllegalArgumentException.class).when(bookRepository).deleteAllInBatch(null);

        assertThrows(IllegalArgumentException.class, () -> bookService.deleteAll(null));
        verify(bookRepository, times(1)).deleteAllInBatch(null);
    }
}
