package com.adproc8.booku.booklist.dto;

import java.sql.Date;
import java.util.UUID;

import lombok.Getter;

@Getter
public class PatchBookRequestDto {
    private UUID id;
    private String publisher;
    private int price;
    private Date publishDate;
    private String isbn;
    private int pageCount;
    private String photoUrl;
    private String category;
}
