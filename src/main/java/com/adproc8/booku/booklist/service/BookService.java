package com.adproc8.booku.booklist.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort.Direction;

import com.adproc8.booku.booklist.model.Book;

public interface BookService {
    Book save(Book book);
    List<Book> saveAll(List<Book> books);
    List<Book> findAll();
    List<Book> findByAuthor(String author);
    List<Book> findByTitle(String title);
    List<Book> findByOrderByPublishDate(Direction direction);
    void deleteById(UUID bookId);
    void deleteAll(List<Book> books);
}
