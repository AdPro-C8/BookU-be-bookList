package com.adproc8.booku.booklist.model;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID bookId;

    @NotNull
    private String title;      // to display
    
    @NotNull
    private String author;     // to display

    @NotNull
    private String publisher;

    @NotNull
    private int price;         // to display

    @NotNull
    private Date publishDate;

    @NotNull
    private String isbn;

    @NotNull
    private int pageCount;

    @NotNull
    private String photoUrl;   // to display

    @Override
    public String toString() {
        return String.format(
            "Book[id=%s, title='%s', author='%s', publisher='%s', price='%d', publishDate='%s', isbn='%s', pageCount='%d', photoUrl='%s']",
            bookId.toString(), title, author, publisher.toString(), price, publishDate.toString(), isbn, pageCount, photoUrl);
    }

}
