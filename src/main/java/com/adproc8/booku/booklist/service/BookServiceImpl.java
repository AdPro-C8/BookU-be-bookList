package com.adproc8.booku.booklist.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public List<Book> saveAll(List<Book> books) throws RuntimeException {
        return bookRepository.saveAll(books);
    }

    public Optional<Book> findById(UUID bookId) {
        return bookRepository.findById(bookId);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAll(Sort sort) {
        return bookRepository.findAll(sort);
    }

    public List<Book> findAll(Specification<Book> spec) {
        return bookRepository.findAll(spec);
    }

    public List<Book> findAll(Specification<Book> spec, Sort sort) {
        return bookRepository.findAll(spec, sort);
    }

    public List<Book> findAllById(Iterable<UUID> ids) {
        return bookRepository.findAllById(ids);
    }

    public void deleteById(UUID bookId) {
        bookRepository.deleteById(bookId);
    }

    public void deleteAll(List<Book> books) {
        bookRepository.deleteAllInBatch(books);
    }
}
