package com.adproc8.booku.booklist.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.repository.BookRepository;

@Service
class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    BookServiceImpl(BookRepository bookRepository) {
    }

    public Book save(Book book) throws RuntimeException {
        return null;
    }

    public List<Book> saveAll(List<Book> books) {
        return null;
    }

    public List<Book> findAll() {
        return null;
    }

    public List<Book> findByAuthor(String author) {
        return null;
    }

    public List<Book> findByTitle(String title) {
        return null;
    }

    public List<Book> findByOrderByPublishDate(Direction direction) {
        return null;
    }

    public void deleteById(UUID bookId) {
    }

    public void deleteAll(List<Book> books) {
    }
}
