package com.adproc8.booku.booklist.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.model.BookView;

public interface BookRepository extends JpaRepository<Book, UUID> {
    List<BookView> findBy();
    List<BookView> findByTitle(String title);
    List<BookView> findByAuthor(String author);
    List<BookView> findByOrderByPublishDateAsc();
    List<BookView> findByOrderByPublishDateDesc();
}
