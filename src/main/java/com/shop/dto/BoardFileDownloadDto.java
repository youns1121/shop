package com.shop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BoardFileDownloadDto {

    private String resource;
    private String contentDisposition;

    @Builder
    public BoardFileDownloadDto(String resource, String contentDisposition) {
        this.resource = resource;
        this.contentDisposition = contentDisposition;
    }
}
