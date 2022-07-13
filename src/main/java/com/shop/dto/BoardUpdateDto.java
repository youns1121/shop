package com.shop.dto;

import com.shop.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardUpdateDto {

    private Long boardId;

    private String boardTitle;

    private String boardContents;

    private String memberEmail;

    private Member member;

    @Builder
    public BoardUpdateDto(Long boardId, String boardTitle, String boardContents, String memberEmail, Member member) {
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.memberEmail = memberEmail;
        this.member = member;
    }
}
