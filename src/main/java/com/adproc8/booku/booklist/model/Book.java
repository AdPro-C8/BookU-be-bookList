package com.adproc8.booku.booklist.model;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID bookId;

    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private Date publishDate;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private int pageCount;

    @Column(nullable = false)
    private String photoUrl;

    @Override
    public String toString() {
        return String.format(
            "Book[id=%s, title='%s', author='%s', publisher='%s', price='%d', publishDate='%s', isbn='%s', pageCount='%d', photoUrl='%s']",
            bookId.toString(), title, author, publisher.toString(), price, publishDate.toString(), isbn, pageCount, photoUrl);
    }

}
