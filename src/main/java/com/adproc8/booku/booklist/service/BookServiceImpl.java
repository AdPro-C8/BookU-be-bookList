package com.adproc8.booku.booklist.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.repository.BookRepository;

@Service
class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book save(Book book) throws RuntimeException {
        return bookRepository.save(book);
    }

    public List<Book> saveAll(List<Book> books) {
        return bookRepository.saveAll(books);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAll(Sort sort) {
        return bookRepository.findAll(sort);
    }

    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> findByAuthor(String author, Sort sort) {
        return bookRepository.findByAuthor(author, sort);
    }

    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> findByTitle(String title, Sort sort) {
        return bookRepository.findByTitle(title, sort);
    }

    public List<Book> findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }

    public List<Book> findByTitleAndAuthor(String title, String author, Sort sort) {
        return bookRepository.findByTitleAndAuthor(title, author, sort);
    }

    public void deleteById(UUID bookId) {
        bookRepository.deleteById(bookId);
    }

    public void deleteAll(List<Book> books) {
        bookRepository.deleteAllInBatch(books);
    }
}
