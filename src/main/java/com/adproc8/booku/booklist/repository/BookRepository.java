package com.adproc8.booku.booklist.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adproc8.booku.booklist.model.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByOrderByPublishDateAsc();
    List<Book> findByOrderByPublishDateDesc();
}
