package com.adproc8.booku.booklist.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class PatchBooksByIdRequestDto {
    private List<PatchBookRequestDto> patchBookDtos;
}
