package com.shop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class BoardUpdateDto {

    private Long boardId;

    private String boardTitle;

    private String boardContents;

    private List<Long> fileIdList;

    @Builder
    public BoardUpdateDto(Long boardId, String boardTitle, String boardContents, List<Long> fileIdList) {
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.fileIdList = fileIdList;
    }
}
