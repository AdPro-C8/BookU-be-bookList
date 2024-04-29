package com.adproc8.booku.booklist.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.adproc8.booku.booklist.model.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByTitle(String title);
    List<Book> findByTitle(String title, Sort sort);
    List<Book> findByAuthor(String author);
    List<Book> findByAuthor(String author, Sort sort);
    List<Book> findByTitleAndAuthor(String title, String author);
    List<Book> findByTitleAndAuthor(String title, String author, Sort sort);
}
