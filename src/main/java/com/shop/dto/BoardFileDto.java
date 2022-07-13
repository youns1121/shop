package com.shop.dto;

import com.shop.domain.Board;
import com.shop.domain.BoardFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class BoardFileDto {

    private Long boardFileId;

    private String boardFileName;

    private long boardFileSize;

    private String boardFilePath;

    private String boardFileStoreName;

    private String boardFileExtension;

    private LocalDateTime createTime;

    private Board board;



    @Builder
    public BoardFileDto(Long boardFileId, String boardFileName, long boardFileSize, String boardFilePath, String boardFileStoreName, String boardFileExtension, Board board, LocalDateTime createTime) {
        this.boardFileId = boardFileId;
        this.boardFileName = boardFileName;
        this.boardFileSize = boardFileSize;
        this.boardFilePath = boardFilePath;
        this.boardFileStoreName = boardFileStoreName;
        this.boardFileExtension = boardFileExtension;
        this.board = board;
        this.createTime = createTime;
    }


    public static BoardFileDto from(BoardFile boardFile){

        return BoardFileDto.builder()
                .boardFileId(boardFile.getBoardFileId())
                .boardFileName(boardFile.getBoardFileName())
                .boardFileSize(boardFile.getBoardFileSize())
                .boardFilePath(boardFile.getBoardFilePath())
                .boardFileExtension(boardFile.getBoardFileExtension())
                .boardFileStoreName(boardFile.getBoardFileStoreName())
                .createTime(boardFile.getCreateTime())
                .build();
    }
}
