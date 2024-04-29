package com.adproc8.booku.booklist.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;

import com.adproc8.booku.booklist.model.Book;

public interface BookService {
    Book save(Book book);
    List<Book> saveAll(List<Book> books);
    List<Book> findAll();
    List<Book> findAll(Sort sort);
    List<Book> findByAuthor(String author);
    List<Book> findByAuthor(String author, Sort sort);
    List<Book> findByTitle(String title);
    List<Book> findByTitle(String title, Sort sort);
    List<Book> findByTitleAndAuthor(String author, String title);
    List<Book> findByTitleAndAuthor(String author, String title, Sort sort);
    void deleteById(UUID bookId);
    void deleteAll(List<Book> books);
}
