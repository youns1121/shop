package com.shop.dto;

import com.shop.domain.Board;
import com.shop.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BoardCreateDto {

    private Long boardId;

    private String boardTitle;

    private String boardContents;

    private String delYn;

    private Member member;

    @Builder
    public BoardCreateDto(Long boardId, String boardTitle, String boardContents, String delYn, Member member) {
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.delYn = delYn;
        this.member = member;
    }

    private static BoardCreateDto from(Board board){

        return BoardCreateDto.builder()
                .boardTitle(board.getBoardTitle())
                .boardContents(board.getBoardContents())
                .delYn(board.getDelYn())
                .member(board.getMember())
                .build();
    }
}
