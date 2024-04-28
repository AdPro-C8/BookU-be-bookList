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

    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> findByOrderByPublishDate(Direction direction) {
        List<Book> books;

        if (direction.isAscending()) {
            books = bookRepository.findByOrderByPublishDateAsc();
        } else {
            books = bookRepository.findByOrderByPublishDateDesc();
        }

        return books;
    }

    public void deleteById(UUID bookId) {
        bookRepository.deleteById(bookId);
    }

    public void deleteAll(List<Book> books) {
        bookRepository.deleteAllInBatch(books);
    }
}
