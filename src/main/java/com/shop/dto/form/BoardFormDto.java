package com.shop.dto.form;

import com.shop.domain.Board;
import com.shop.domain.Board;
import com.shop.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
public class BoardFormDto {

    private Long boardId;

    @NotBlank(message = "제목은 필수값 입니다.")
    private String boardTitle;

    @NotBlank(message = "내용은 필수값 입니다.")
    private String boardContents;

    private String delYn;

    private Board board;

    private Member member;

    @Builder
    public BoardFormDto(Long boardId, String boardTitle, String boardContents, String delYn, Board board, Member member) {
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.delYn = delYn;
        this.board = board;
        this.member = member;
    }

    private static BoardFormDto from(Board board){

        return BoardFormDto.builder()
                .boardTitle(board.getBoardTitle())
                .boardContents(board.getBoardContents())
                .delYn(board.getDelYn())
                .member(board.getMember())
                .build();
    }
}
