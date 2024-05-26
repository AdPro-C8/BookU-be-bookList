package com.adproc8.booku.booklist.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.adproc8.booku.booklist.model.Book;

public interface BookService {
    Book save(Book book);
    List<Book> saveAll(List<Book> books);
    Optional<Book> findById(UUID bookId);
    List<Book> findAll();
    List<Book> findAll(Sort sort);
    List<Book> findAll(Specification<Book> spec);
    List<Book> findAll(Specification<Book> spec, Sort sort);
    List<Book> findAllById(Iterable<UUID> ids);
    List<Book> findAllById(Set<UUID> ids, Specification<Book> spec);
    void deleteById(UUID bookId);
    void deleteAll(List<Book> books);
}
