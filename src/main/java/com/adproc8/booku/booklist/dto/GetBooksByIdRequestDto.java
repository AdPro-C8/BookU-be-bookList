package com.adproc8.booku.booklist.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class GetBooksByIdRequestDto {
    private List<UUID> bookIds;
}
