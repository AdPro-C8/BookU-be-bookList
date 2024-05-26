package com.adproc8.booku.booklist.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class RatingByBookIdDto {
    private UUID bookId;
    private float averageRating;
}
