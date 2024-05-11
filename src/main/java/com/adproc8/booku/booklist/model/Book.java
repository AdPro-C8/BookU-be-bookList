package com.adproc8.booku.booklist.model;

import java.sql.Date;
import java.util.UUID;

import org.hibernate.annotations.Check;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    @Check(constraints = "price >= 0")
    private int price;

    @Column(nullable = false)
    private Date publishDate;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    @Check(constraints = "page_count >= 0")
    private int pageCount;

    @Column(nullable = false)
    private String photoUrl;
}
