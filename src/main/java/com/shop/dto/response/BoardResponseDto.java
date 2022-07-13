package com.shop.dto.response;

import com.shop.domain.Board;
import com.shop.dto.BoardFileDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Builder
    public BoardResponseDto(Long boardId, Long memberId, String boardWriter, String boardTitle, String boardContents, String delYn, List<BoardFileDto> boardFileDtoList, LocalDateTime createTime, LocalDateTime updateTime) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.delYn = delYn;
        this.boardFileDtoList = boardFileDtoList;
        this.createTime = createTime;
        this.updateTime = updateTime;
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
}
