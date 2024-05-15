package com.adproc8.booku.booklist.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostBookResponseDto {
    private UUID bookId;
}
