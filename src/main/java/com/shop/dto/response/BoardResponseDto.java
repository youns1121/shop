package com.shop.dto.response;

import com.shop.domain.Board;
import com.shop.dto.BoardFileDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class BoardResponseDto {

    private Long boardId;

    private Long memberId;

    private String boardWriter;

    private String boardTitle;

    private String boardContents;

    private String delYn;

    private List<BoardFileDto> boardFileDtoList;

    private List<Long> fileIdList;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Builder
    public BoardResponseDto(Long boardId, Long memberId, String boardWriter, String boardTitle, String boardContents, String delYn, List<BoardFileDto> boardFileDtoList, List<Long> fileIdList, LocalDateTime createTime, LocalDateTime updateTime) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.delYn = delYn;
        this.boardFileDtoList = boardFileDtoList;
        this.fileIdList = fileIdList;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public static List<BoardResponseDto> from(List<Board> boardList){

        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        for(Board board : boardList) {
           boardResponseDtoList.add(
                   BoardResponseDto.builder()
                    .boardId(board.getBoardId())
                    .memberId(board.getMember().getId())
                    .boardWriter(board.getMember().getName())
                    .boardTitle(board.getBoardTitle())
                    .boardContents(board.getBoardContents())
                    .createTime(board.getCreateTime())
                    .updateTime(board.getUpdateTime())
                    .delYn(board.getDelYn())
                    .build()
           );
        }

        return boardResponseDtoList;
    }

    public static BoardResponseDto of(Board board, List<BoardFileDto> boardFileDtoList){

        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .memberId(board.getMember().getId())
                .boardWriter(board.getMember().getName())
                .boardTitle(board.getBoardTitle())
                .boardContents(board.getBoardContents())
                .boardFileDtoList(boardFileDtoList)
                .delYn(board.getDelYn())
                .build();
    }

    public List<BoardFileDto> getBoardFileDtoList() {
        return boardFileDtoList == null ? Collections.emptyList() : boardFileDtoList;
    }
}
