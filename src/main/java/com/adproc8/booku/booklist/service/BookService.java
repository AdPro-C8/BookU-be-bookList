package com.adproc8.booku.booklist.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.adproc8.booku.booklist.model.Book;

public interface BookService {
    Book save(Book book);
    List<Book> saveAll(List<Book> books);
    List<Book> findAll();
    List<Book> findAll(Sort sort);
    List<Book> findAll(Specification<Book> spec);
    List<Book> findAll(Specification<Book> spec, Sort sort);
    void deleteById(UUID bookId);
    void deleteAll(List<Book> books);
}
